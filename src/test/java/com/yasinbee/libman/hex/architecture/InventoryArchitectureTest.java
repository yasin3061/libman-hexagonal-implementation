package com.yasinbee.libman.hex.architecture;

import com.tngtech.archunit.core.importer.ImportOption;
import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;
import com.yasinbee.libman.hex.domain.inventory.core.InventoryFacade;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClass;
import static com.tngtech.archunit.library.Architectures.onionArchitecture;

@AnalyzeClasses(packages = {"com.yasinbee.libman.hex.domain.inventory"},
        importOptions = {ImportOption.DoNotIncludeTests.class})
public class InventoryArchitectureTest {

    @ArchTest
    public static final ArchRule hexagonalArchInInventoryDomain = onionArchitecture()
            .domainModels("com.yasinbee.libman.hex.domain.inventory.core.model..")
            .domainServices("com.yasinbee.libman.hex.domain.inventory..")
            .applicationServices("com.yasinbee.libman.hex.domain.inventory.application..")
            .adapter("infrastructure", "com.yasinbee.libman.hex.domain.inventory.infrastructure..");

    @ArchTest
    public static final ArchRule noSpringDependenciesInInventoryFacade =
            noClass(InventoryFacade.class)
                    .should()
                    .dependOnClassesThat()
                    .resideInAPackage("org.springframework..");

}
