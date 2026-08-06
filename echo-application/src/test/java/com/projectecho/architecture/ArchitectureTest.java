package com.projectecho.architecture;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;
import static com.tngtech.archunit.library.Architectures.layeredArchitecture;

import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;

@AnalyzeClasses(packages = "com.projectecho")
public class ArchitectureTest {

    private static final String[] DOMAIN_PACKAGES = {
        "com.projectecho.identity.domain..",
        "com.projectecho.taxonomy.domain..",
        "com.projectecho.evidence.domain..",
        "com.projectecho.intelligence.domain..",
        "com.projectecho.mission.domain..",
        "com.projectecho.ruleengine.domain.."
    };

    private static final String[] INFRASTRUCTURE_PACKAGES = {
        "com.projectecho.identity.infrastructure..",
        "com.projectecho.taxonomy.infrastructure..",
        "com.projectecho.evidence.infrastructure..",
        "com.projectecho.intelligence.infrastructure..",
        "com.projectecho.mission.infrastructure..",
        "com.projectecho.ruleengine.infrastructure.."
    };

    private static final String[] PRESENTATION_PACKAGES = {
        "com.projectecho.identity.presentation..",
        "com.projectecho.taxonomy.presentation..",
        "com.projectecho.evidence.presentation..",
        "com.projectecho.intelligence.presentation..",
        "com.projectecho.mission.presentation..",
        "com.projectecho.ruleengine.presentation.."
    };

    private static final String[] APPLICATION_PACKAGES = {
        "com.projectecho.identity.application..",
        "com.projectecho.taxonomy.application..",
        "com.projectecho.evidence.application..",
        "com.projectecho.intelligence.application..",
        "com.projectecho.mission.application..",
        "com.projectecho.ruleengine.application.."
    };

    @ArchTest
    public static final ArchRule layer_dependencies_are_respected =
            layeredArchitecture()
                    .consideringAllDependencies()
                    .withOptionalLayers(true)
                    .layer("Presentation")
                    .definedBy(PRESENTATION_PACKAGES)
                    .layer("Application")
                    .definedBy(APPLICATION_PACKAGES)
                    .layer("Domain")
                    .definedBy(DOMAIN_PACKAGES)
                    .layer("Infrastructure")
                    .definedBy(INFRASTRUCTURE_PACKAGES)
                    .whereLayer("Presentation")
                    .mayNotBeAccessedByAnyLayer()
                    .whereLayer("Application")
                    .mayOnlyBeAccessedByLayers("Presentation", "Infrastructure")
                    .whereLayer("Infrastructure")
                    .mayNotBeAccessedByAnyLayer()
                    .whereLayer("Domain")
                    .mayOnlyBeAccessedByLayers("Application", "Infrastructure", "Presentation");

    @ArchTest
    public static final ArchRule identity_must_not_depend_on_taxonomy =
            noClasses()
                    .that()
                    .resideInAPackage("com.projectecho.identity..")
                    .should()
                    .dependOnClassesThat()
                    .resideInAPackage("com.projectecho.taxonomy..");

    @ArchTest
    public static final ArchRule taxonomy_must_not_depend_on_identity =
            noClasses()
                    .that()
                    .resideInAPackage("com.projectecho.taxonomy..")
                    .should()
                    .dependOnClassesThat()
                    .resideInAPackage("com.projectecho.identity..");

    @ArchTest
    public static final ArchRule evidence_must_not_depend_on_identity_or_taxonomy =
            noClasses()
                    .that()
                    .resideInAPackage("com.projectecho.evidence..")
                    .should()
                    .dependOnClassesThat()
                    .resideInAnyPackage("com.projectecho.identity..", "com.projectecho.taxonomy..");
}
