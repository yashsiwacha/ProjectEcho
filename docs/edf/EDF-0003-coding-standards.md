# EDF-0003: Coding Standards

**Document ID:** EDF-0003
**Status:** FROZEN
**Version:** 1.0

---

## 1. Java 21 Conventions
- **Records**: Use Java `record` for all DTOs, Value Objects, and Integration Events.
- **Pattern Matching**: Utilize pattern matching for `switch` and `instanceof`.
- **Sealed Classes**: Use `sealed` interfaces to represent bounded domain states (e.g., `public sealed interface TrustTier permits High, Medium, Low`).
- **Nullability**: No `null` returns in the domain layer. Use `java.util.Optional` strictly for return types, never for fields or parameters.

## 2. Spring Boot Conventions
- **Constructor Injection**: `@Autowired` on fields is strictly forbidden. Use `final` fields with constructor injection (or Lombok `@RequiredArgsConstructor`).
- **Configuration**: Use `@ConfigurationProperties` over `@Value` to ensure type-safe external configuration binding.

## 3. Global Exception Handling
- **Hierarchy**: All domain exceptions extend a base `DomainException` (defined in `echo-shared`).
- **REST Translation**: A single `@RestControllerAdvice` in `echo-application` translates `DomainException` to `ProblemDetail` (RFC 7807). No HTTP status codes shall leak into the domain layer.

## 4. Logging & Observability
- **Library**: SLF4J with Logback.
- **Structured Logging**: All logs must be output in JSON format.
- **MDC (Mapped Diagnostic Context)**: `CorrelationId` and `PassportId` (if authenticated) must be injected into the MDC at the entry point of every HTTP request or asynchronous event listener.
- **Constraint**: PII (Personally Identifiable Information) must be masked via Logback filters.
