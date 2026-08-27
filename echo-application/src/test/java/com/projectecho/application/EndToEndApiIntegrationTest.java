package com.projectecho.application;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import java.util.Map;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ActiveProfiles;

@Tag("integration")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class EndToEndApiIntegrationTest {

    @LocalServerPort private int port;

    public EndToEndApiIntegrationTest() {
        super();
    }

    @BeforeEach
    void setUpRestAssured() {
        RestAssured.port = port;
    }

    @Test
    void executeCompleteBackendWorkflow() {
        // 0. Sign Up
        final String email = "testadmin." + UUID.randomUUID() + "@echo.local";
        final String signupPayload =
                "{\"username\":\"testadmin\", \"email\":\""
                        + email
                        + "\", \"password\":\"testadminpwd\", \"name\":\"Test Admin\"}";
        final io.restassured.response.Response authResponse =
                given().contentType(ContentType.JSON)
                        .body(signupPayload)
                        .when()
                        .post("/api/v1/auth/signup")
                        .then()
                        .statusCode(HttpStatus.OK.value())
                        .extract()
                        .response();

        final String tokenCookie = authResponse.getCookie("echo_jwt");

        // 1. Initialize Career Passport
        final String passportId =
                given().cookie("echo_jwt", tokenCookie)
                        .contentType(ContentType.JSON)
                        .body(
                                Map.of(
                                        "name",
                                        "Test Admin",
                                        "email",
                                        email,
                                        "jobTitle",
                                        "Senior Software Engineer"))
                        .when()
                        .post("/api/v1/passports")
                        .then()
                        .statusCode(201)
                        .body("id", notNullValue())
                        .body("name", equalTo("Test Admin"))
                        .body("email", equalTo(email))
                        .extract()
                        .path("id");

        // 2. Register Skill
        final String skillName = "Java 21 - " + UUID.randomUUID();
        final String skillId =
                given().cookie("echo_jwt", tokenCookie)
                        .contentType(ContentType.JSON)
                        .body(Map.of("name", skillName, "category", "Backend Engineering"))
                        .when()
                        .post("/api/v1/skills")
                        .then()
                        .statusCode(201)
                        .body("id", notNullValue())
                        .body("name", equalTo(skillName))
                        .extract()
                        .path("id");

        // 3. Submit Evidence Claim
        final String evidenceId =
                given().cookie("echo_jwt", tokenCookie)
                        .contentType(ContentType.JSON)
                        .body(
                                Map.of(
                                        "passportId", passportId,
                                        "skillId", skillId,
                                        "sourceUri", "https://github.com/projectecho/repo"))
                        .when()
                        .post("/api/v1/evidence")
                        .then()
                        .statusCode(201)
                        .body("id", notNullValue())
                        .body("validationStatus", equalTo("PENDING"))
                        .extract()
                        .path("id");

        // 4. Verify Evidence Claim
        given().cookie("echo_jwt", tokenCookie)
                .contentType(ContentType.JSON)
                .body(Map.of("trustTier", "TIER_4"))
                .when()
                .put("/api/v1/evidence/" + evidenceId + "/verify")
                .then()
                .statusCode(200)
                .body("validationStatus", equalTo("VERIFIED"))
                .body("trustTier", equalTo("TIER_4"));

        // 5. Create & Activate Mission
        final String missionId =
                given().cookie("echo_jwt", tokenCookie)
                        .contentType(ContentType.JSON)
                        .body(Map.of("title", "Lead System Architect Mission"))
                        .when()
                        .post("/api/v1/missions")
                        .then()
                        .statusCode(201)
                        .body("id", notNullValue())
                        .body("status", equalTo("DRAFT"))
                        .extract()
                        .path("id");

        given().cookie("echo_jwt", tokenCookie)
                .when()
                .put("/api/v1/missions/" + missionId + "/activate")
                .then()
                .statusCode(200)
                .body("status", equalTo("ACTIVE"));

        // 6. Trigger Readiness Assessment via Rule Engine
        given().cookie("echo_jwt", tokenCookie)
                .contentType(ContentType.JSON)
                .body(
                        Map.of(
                                "passportId",
                                passportId,
                                "missionId",
                                missionId,
                                "passportSkills",
                                java.util.Set.of("Java 21"),
                                "isPassportVerified",
                                true,
                                "missionRequiredSkills",
                                java.util.Set.of("Java 21"),
                                "isMissionActive",
                                true))
                .when()
                .post("/api/v1/assessments/evaluate")
                .then()
                .statusCode(201)
                .body("eligible", equalTo(true))
                .body("score", equalTo(100));

        // 7. Verify Reasoning Card query endpoint
        given().cookie("echo_jwt", tokenCookie)
                .when()
                .get("/api/v1/reasoning-cards?passportId=" + passportId)
                .then()
                .statusCode(200);
    }
}
