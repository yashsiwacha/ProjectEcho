package com.projectecho.application.security;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

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

@Tag("security")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class CrossUserAuthorizationTest {

    @LocalServerPort private int port;

    @BeforeEach
    void setUpRestAssured() {
        RestAssured.port = port;
    }

    private String signupAndGetCookie(final String username, final String email) {
        final String signupPayload =
                String.format(
                        "{\"username\":\"%s\", \"email\":\"%s\", \"password\":\"password123\", \"name\":\"%s\"}",
                        username, email, username);

        final io.restassured.response.Response response =
                given().contentType(ContentType.JSON)
                        .body(signupPayload)
                        .when()
                        .post("/api/v1/auth/signup")
                        .then()
                        .statusCode(HttpStatus.OK.value())
                        .extract()
                        .response();

        return response.getCookie("echo_jwt");
    }

    private String createPassport(final String cookie, final String name, final String email) {
        return given().cookie("echo_jwt", cookie)
                .contentType(ContentType.JSON)
                .body(Map.of("name", name, "email", email, "jobTitle", "Engineer"))
                .when()
                .post("/api/v1/passports")
                .then()
                .statusCode(201)
                .extract()
                .path("id");
    }

    private String createMission(final String cookie, final String title) {
        return given().cookie("echo_jwt", cookie)
                .contentType(ContentType.JSON)
                .body(Map.of("title", title))
                .when()
                .post("/api/v1/missions")
                .then()
                .statusCode(201)
                .extract()
                .path("id");
    }

    @Test
    void executeAliceBobCrossUserAttack() {
        // 1. Setup Alice
        final String aliceEmail = "alice." + UUID.randomUUID() + "@echo.local";
        final String aliceCookie = signupAndGetCookie("alice", aliceEmail);
        final String alicePassportId = createPassport(aliceCookie, "Alice", aliceEmail);
        final String aliceMissionId = createMission(aliceCookie, "Alice's Secret Mission");

        // 2. Setup Bob
        final String bobEmail = "bob." + UUID.randomUUID() + "@echo.local";
        final String bobCookie = signupAndGetCookie("bob", bobEmail);
        final String bobPassportId = createPassport(bobCookie, "Bob", bobEmail);
        final String bobMissionId = createMission(bobCookie, "Bob's Public Mission");

        // 3. Test Alice accessing Bob's Passport (IDOR attack)
        given().cookie("echo_jwt", aliceCookie)
                .when()
                .get("/api/v1/passports/" + bobPassportId)
                .then()
                .statusCode(HttpStatus.FORBIDDEN.value()); // Alice should get 403 on Bob's Passport

        // 4. Test Bob accessing Alice's Passport (IDOR attack)
        given().cookie("echo_jwt", bobCookie)
                .when()
                .get("/api/v1/passports/" + alicePassportId)
                .then()
                .statusCode(HttpStatus.FORBIDDEN.value()); // Bob should get 403 on Alice's Passport

        // 5. Test Alice accessing Bob's Mission (IDOR attack)
        given().cookie("echo_jwt", aliceCookie)
                .when()
                .get("/api/v1/missions/" + bobMissionId)
                .then()
                .statusCode(HttpStatus.FORBIDDEN.value()); // Alice should get 403 on Bob's Mission

        // 6. Test Bob modifying Alice's Mission (IDOR attack)
        given().cookie("echo_jwt", bobCookie)
                .when()
                .put("/api/v1/missions/" + aliceMissionId + "/activate")
                .then()
                .statusCode(HttpStatus.FORBIDDEN.value()); // Bob should get 403 on Alice's Mission

        // 7. Verify Alice can access Alice's stuff
        given().cookie("echo_jwt", aliceCookie)
                .when()
                .get("/api/v1/missions/" + aliceMissionId)
                .then()
                .statusCode(HttpStatus.OK.value())
                .body("title", equalTo("Alice's Secret Mission"));

        // 8. Verify Bob can access Bob's stuff
        given().cookie("echo_jwt", bobCookie)
                .when()
                .get("/api/v1/passports/" + bobPassportId)
                .then()
                .statusCode(HttpStatus.OK.value())
                .body("name", equalTo("Bob"));
    }
}
