package com.yasinbee.libman.hex.architecture;

import com.tngtech.archunit.core.importer.ImportOption;
import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;
import com.yasinbee.libman.hex.domain.email.core.EmailFacade;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClass;
import static com.tngtech.archunit.library.Architectures.onionArchitecture;

@AnalyzeClasses(packages = {"com.yasinbee.libman.hex.domain.email"},
        importOptions = { ImportOption.DoNotIncludeTests.class })
public class EmailArchitectureTest {

    @ArchTest
    public static final ArchRule hexagonalArchInEmailDomain = onionArchitecture()
            .domainModels("com.yasinbee.libman.hex.domain.email.core.model..")
            .domainServices("com.yasinbee.libman.hex.domain.email..")
            .applicationServices("com.yasinbee.libman.hex.domain.email.application..")
            .adapter("infrastructure", "com.yasinbee.libman.hex.domain.email.infrastructure..");

    @ArchTest
    public static final ArchRule noSpringDependenciesInEmailFacade =
            noClass(EmailFacade.class)
                    .should()
                    .dependOnClassesThat()
                    .resideInAPackage("org.springframework..");
}
