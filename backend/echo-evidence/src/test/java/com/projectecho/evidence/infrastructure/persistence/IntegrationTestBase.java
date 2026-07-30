package com.projectecho.evidence.infrastructure.persistence;

import jakarta.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;

/**
 * Foundation for all Persistence Integration Tests in the Evidence bounded context.
 *
 * <p>Features: - Uses a shared (singleton) PostgreSQL Testcontainer to minimize startup time. -
 * Enforces the "test" profile which runs Flyway scripts and sets Hibernate to validate schema. -
 * Sets AutoConfigureTestDatabase.Replace.NONE to prevent Spring from using an embedded database
 * (like H2). - Provides common utilities (flushAndClear) for JPA state manipulation.
 */
@DataJpaTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@org.springframework.context.annotation.Import({
    com.projectecho.evidence.infrastructure.persistence.repository.JpaEvidenceLineageRepositoryAdapter.class,
    IntegrationTestBase.TestConfig.class
})
public abstract class IntegrationTestBase {

  // The singleton container, starts only once per JVM.
  private static final PostgreSQLContainer<?> postgres =
      new PostgreSQLContainer<>("postgres:16-alpine")
          .withDatabaseName("echo_evidence_test")
          .withUsername("echo_test")
          .withPassword("echo_test");

  static {
    postgres.start();
  }

  @DynamicPropertySource
  static void configureProperties(DynamicPropertyRegistry registry) {
    registry.add("spring.datasource.url", postgres::getJdbcUrl);
    registry.add("spring.datasource.username", postgres::getUsername);
    registry.add("spring.datasource.password", postgres::getPassword);
  }

  @Autowired protected EntityManager entityManager;

  /**
   * Utility to force JPA to synchronize state with the database and clear the L1 cache. This is
   * essential for simulating fresh transaction reads and verifying actual persistence, as opposed
   * to just reading from Hibernate's Session cache.
   */
  protected void flushAndClear() {
    entityManager.flush();
    entityManager.clear();
  }

  /**
   * Helper to generate isolated IDs for testing. Guaranteed uniqueness prevents data collisions
   * during parallel execution.
   */
  protected com.projectecho.common.valueobject.Identifier generateIsolatedId() {
    return com.projectecho.common.valueobject.Identifier.generate();
  }

  @org.springframework.boot.test.context.TestConfiguration
  public static class TestConfig {
      @org.springframework.context.annotation.Bean
      public com.fasterxml.jackson.databind.ObjectMapper objectMapper() {
          com.fasterxml.jackson.databind.ObjectMapper mapper = new com.fasterxml.jackson.databind.ObjectMapper();
          mapper.registerModule(new com.fasterxml.jackson.datatype.jsr310.JavaTimeModule());
          mapper.disable(com.fasterxml.jackson.databind.SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
          mapper.setVisibility(com.fasterxml.jackson.annotation.PropertyAccessor.ALL, com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.ANY);
          return mapper;
      }
  }
}
