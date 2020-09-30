package com.yasinbee.libman.hex.architecture;

import com.tngtech.archunit.core.importer.ImportOption;
import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;
import com.yasinbee.libman.hex.domain.user.core.UserFacade;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClass;
import static com.tngtech.archunit.library.Architectures.onionArchitecture;

@AnalyzeClasses(packages = {"com.yasinbee.libman.hex.domain.user"},
        importOptions = { ImportOption.DoNotIncludeTests.class })
public class UserArchitectureTest {

    @ArchTest
    public static final ArchRule hexagonalArchInUserDomain = onionArchitecture()
            .domainModels("com.yasinbee.libman.hex.domain.user.core.model..")
            .domainServices("com.yasinbee.libman.hex.domain.user..")
            .applicationServices("com.yasinbee.libman.hex.domain.user.application..")
            .adapter("infrastructure", "com.yasinbee.libman.hex.domain.user.infrastructure..");

    @ArchTest
    public static final ArchRule noSpringDependenciesInUserFacade =
            noClass(UserFacade.class)
                    .should()
                    .dependOnClassesThat()
                    .resideInAPackage("org.springframework..");
}
