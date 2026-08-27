## ---- Backend Dockerfile (Multi-stage) ----
# Builder stage
FROM maven:3.9-eclipse-temurin-21 AS builder
WORKDIR /src
COPY . /src
RUN --mount=type=cache,target=/root/.m2 mvn -B -DskipTests clean package

# Runtime stage
FROM eclipse-temurin:21-jre
WORKDIR /app
COPY --from=builder /src/echo-application/target/echo-application-1.0.0-SNAPSHOT.jar ./app.jar
# OpenTelemetry Java agent (download at runtime)
RUN wget -O /otel-javaagent.jar https://github.com/open-telemetry/opentelemetry-java-instrumentation/releases/download/v1.33.0/opentelemetry-javaagent.jar && chmod 644 /otel-javaagent.jar
# Create non‑root user
RUN addgroup --system appgroup && adduser --system --ingroup appgroup appuser
USER appuser
# Only set profile — DB credentials come from Render environment variables
ENV SPRING_PROFILES_ACTIVE=prod
EXPOSE 8081
HEALTHCHECK --interval=30s --timeout=10s --start-period=90s CMD wget -qO- http://localhost:8081/actuator/health || exit 1
ENTRYPOINT ["java", "-XX:+UseContainerSupport", "-XX:MaxRAMPercentage=75.0", "-javaagent:/otel-javaagent.jar", "-jar", "/app/app.jar"]
