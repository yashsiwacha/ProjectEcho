package com.projectecho.application;

import org.junit.jupiter.api.Tag;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * Base class for all Integration Tests. Ensures tests are tagged correctly for
 * maven-failsafe-plugin.
 */
@Tag("integration")
@SpringBootTest
public abstract class BaseIntegrationTest {}
