package com.projectecho.architecture;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes;
import static com.tngtech.archunit.library.Architectures.layeredArchitecture;
import static com.tngtech.archunit.library.dependencies.SlicesRuleDefinition.slices;

import com.tngtech.archunit.core.importer.ImportOption;
import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;

/**
 * Global Architectural Fitness Functions for ProjectEcho. Enforces the Dependency Matrix and
 * structural rules defined in the EAD.
 */
@AnalyzeClasses(packages = "com.projectecho", importOptions = ImportOption.DoNotIncludeTests.class)
public class ArchitectureTests {

  @ArchTest
  static final ArchRule enforceDependencyMatrix =
      layeredArchitecture()
          .consideringAllDependencies()
          .withOptionalLayers(true)
          .layer("Common")
          .definedBy("com.projectecho.common..")
          .layer("Identity")
          .definedBy("com.projectecho.identity..")
          .layer("Passport")
          .definedBy("com.projectecho.passport..")
          .layer("Competency")
          .definedBy("com.projectecho.competency..")
          .layer("Evidence")
          .definedBy("com.projectecho.evidence..")
          .layer("Intelligence")
          .definedBy("com.projectecho.intelligence..")
          .layer("Bootstrap")
          .definedBy("com.projectecho.bootstrap..")

          // 1. echo-common sits at the bottom, accessible by everyone
          .whereLayer("Common")
          .mayOnlyBeAccessedByLayers(
              "Identity", "Passport", "Competency", "Evidence", "Intelligence", "Bootstrap")

          // 2. Identity is pure infrastructure, accessed only by Bootstrap
          .whereLayer("Identity")
          .mayOnlyBeAccessedByLayers("Bootstrap")

          // 3. Core domain modules
          .whereLayer("Passport")
          .mayOnlyBeAccessedByLayers("Intelligence", "Bootstrap")
          .whereLayer("Competency")
          .mayOnlyBeAccessedByLayers("Evidence", "Intelligence", "Bootstrap")

          // 4. Evidence depends on Competency, accessed by Intelligence and Bootstrap
          .whereLayer("Evidence")
          .mayOnlyBeAccessedByLayers("Intelligence", "Bootstrap")

          // 5. Intelligence orchestrates across domain
          .whereLayer("Intelligence")
          .mayOnlyBeAccessedByLayers("Bootstrap")

          // 6. Bootstrap is the monolithic entry point
          .whereLayer("Bootstrap")
          .mayNotBeAccessedByAnyLayer();

  @ArchTest
  static final ArchRule noCyclicDependencies =
      slices().matching("com.projectecho.(*)..").should().beFreeOfCycles();

  @ArchTest
  static final ArchRule controllersMustResideInBootstrap =
      classes()
          .that()
          .areAnnotatedWith("org.springframework.web.bind.annotation.RestController")
          .should()
          .resideInAPackage("com.projectecho.bootstrap..");
}
