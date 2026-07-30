package com.projectecho.evidence.architecture;

import com.tngtech.archunit.core.importer.ImportOption;
import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes;
import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;
import static com.tngtech.archunit.library.Architectures.layeredArchitecture;
import static com.tngtech.archunit.library.dependencies.SlicesRuleDefinition.slices;

@AnalyzeClasses(packages = "com.projectecho.evidence", importOptions = ImportOption.DoNotIncludeTests.class)
public class EvidenceArchitectureTest {

    // 1. Domain layer has no dependency on Spring, JPA, Jackson, Infrastructure, Web
    @ArchTest
    static final ArchRule domain_layer_must_not_depend_on_infrastructure_or_frameworks = noClasses()
            .that().resideInAPackage("..domain..")
            .should().dependOnClassesThat().resideInAnyPackage(
                    "org.springframework..",
                    "jakarta.persistence..",
                    "com.fasterxml.jackson..",
                    "com.projectecho.evidence.infrastructure..",
                    "com.projectecho.evidence.application..",
                    "com.projectecho.evidence.presentation.."
            )
            .because("The Domain Layer must be pure business logic completely decoupled from infrastructure or frameworks.");

    // 2. Infrastructure cannot be accessed directly by higher layers
    @ArchTest
    static final ArchRule infrastructure_must_not_be_accessed_by_domain_or_application = noClasses()
            .that().resideInAnyPackage("..domain..", "..application..")
            .should().dependOnClassesThat().resideInAPackage("..infrastructure..")
            .because("Infrastructure details must be inverted. Higher layers should only use domain ports.");

    // 3. Repository adapters implement the corresponding domain repository interfaces
    @ArchTest
    static final ArchRule repository_adapters_must_implement_domain_repositories = classes()
            .that().resideInAPackage("..infrastructure.persistence.repository..")
            .and().haveSimpleNameEndingWith("Adapter")
            .should().implement(com.projectecho.evidence.domain.repository.EvidenceLineageRepository.class)
            .because("Repository adapters must satisfy the ports defined by the domain model.");

    // 4. Domain model classes contain no JPA annotations
    @ArchTest
    static final ArchRule domain_models_must_not_be_jpa_entities = noClasses()
            .that().resideInAPackage("..domain.model..")
            .should().beAnnotatedWith("jakarta.persistence.Entity")
            .orShould().beAnnotatedWith("jakarta.persistence.Table")
            .because("Domain models should not be polluted with persistence mechanisms.");

    // 5. JPA entities are confined to the infrastructure.persistence package
    @ArchTest
    static final ArchRule jpa_entities_must_reside_in_infrastructure_persistence = classes()
            .that().areAnnotatedWith("jakarta.persistence.Entity")
            .should().resideInAPackage("..infrastructure.persistence.entity..")
            .because("JPA entities are infrastructure-specific persistence models and must not leak into the domain.");

    // 6. Application layer depends only on Domain abstractions
    @ArchTest
    static final ArchRule application_layer_depends_only_on_domain = classes()
            .that().resideInAPackage("..application..")
            .should().onlyDependOnClassesThat().resideInAnyPackage(
                    "java..",
                    "com.projectecho.evidence.domain..",
                    "com.projectecho.evidence.application..",
                    "com.projectecho.common.."
            )
            .because("The Application Layer orchestrates domain logic and should not depend on UI or persistence layers.");

    // 7. No cyclic package dependencies exist
    @ArchTest
    static final ArchRule packages_must_be_free_of_cycles = slices()
            .matching("com.projectecho.evidence.(*)..")
            .should().beFreeOfCycles()
            .because("Cyclic dependencies lead to tight coupling and poor maintainability.");

    // 8. Domain Events remain free of infrastructure dependencies
    @ArchTest
    static final ArchRule domain_events_must_be_pure = classes()
            .that().implement("com.projectecho.common.event.DomainEvent")
            .should().onlyDependOnClassesThat().resideInAnyPackage(
                    "java..",
                    "com.projectecho.evidence.domain..",
                    "com.projectecho.common.."
            )
            .because("Domain Events represent pure business occurrences and must remain infrastructure-agnostic.");

    // 9. Test code does not leak into production packages
    @ArchTest
    static final ArchRule production_code_must_not_depend_on_test_code = noClasses()
            .that().resideInAPackage("com.projectecho.evidence..")
            .should().dependOnClassesThat().haveSimpleNameEndingWith("Test")
            .orShould().dependOnClassesThat().haveSimpleNameEndingWith("Tests")
            .because("Production code must remain free of test logic.");

    // 10. Package structure conforms to the Engineering Architecture Document (EAD)
    @ArchTest
    static final ArchRule layered_architecture_is_respected = layeredArchitecture()
            .consideringOnlyDependenciesInLayers()
            .layer("Domain").definedBy("..domain..")
            .layer("Application").definedBy("..application..")
            .layer("Infrastructure").definedBy("..infrastructure..")
            // Optional Presentation Layer
            
            .whereLayer("Application").mayOnlyBeAccessedByLayers("Infrastructure")
            .whereLayer("Domain").mayOnlyBeAccessedByLayers("Application", "Infrastructure")
            .because("The architectural layers must strictly adhere to the Dependency Inversion Principle as defined in the EAD.");
}
