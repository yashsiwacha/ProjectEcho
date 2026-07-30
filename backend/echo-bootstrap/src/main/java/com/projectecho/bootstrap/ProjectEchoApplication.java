package com.projectecho.bootstrap;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Main entry point for the ProjectEcho Modular Monolith. Scans components across all defined
 * bounded contexts in the com.projectecho namespace.
 */
@SpringBootApplication(scanBasePackages = "com.projectecho")
public class ProjectEchoApplication {

  public static void main(String[] args) {
    SpringApplication.run(ProjectEchoApplication.class, args);
  }
}
