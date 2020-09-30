package com.yasinbee.libman.hex.architecture;

import com.tngtech.archunit.core.importer.ImportOption;
import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;
import com.yasinbee.libman.hex.domain.borrowing.core.BorrowingFacade;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClass;
import static com.tngtech.archunit.library.Architectures.onionArchitecture;

@AnalyzeClasses(packages = {"com.yasinbee.libman.hex.domain.borrowing"},
                importOptions = { ImportOption.DoNotIncludeTests.class })
public class BorrowingArchitectureTest {

    @ArchTest
    public static final ArchRule hexagonalArchInBorrowingDomain = onionArchitecture()
            .domainModels("com.yasinbee.libman.hex.domain.borrowing.core.model..")
            .domainServices("com.yasinbee.libman.hex.domain.borrowing..")
            .applicationServices("com.yasinbee.libman.hex.domain.borrowing.application..")
            .adapter("infrastructure", "com.yasinbee.libman.hex.domain.borrowing.infrastructure..");

    @ArchTest
    public static final ArchRule noSpringDependenciesInBorrowingFacade =
            noClass(BorrowingFacade.class)
                    .should()
                    .dependOnClassesThat()
                    .resideInAPackage("org.springframework..");
}
