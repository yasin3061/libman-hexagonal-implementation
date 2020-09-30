package com.yasinbee.libman.hex;

import com.yasinbee.libman.hex.infrastructure.BorrowingDomainConfig;
import com.yasinbee.libman.hex.infrastructure.EmailDomainConfig;
import com.yasinbee.libman.hex.infrastructure.InventoryDomainConfig;
import com.yasinbee.libman.hex.infrastructure.LibraryHexagonalConfig;
import com.yasinbee.libman.hex.infrastructure.UserDomainConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
@Import({
		LibraryHexagonalConfig.class,
		InventoryDomainConfig.class,
		BorrowingDomainConfig.class,
		EmailDomainConfig.class,
		UserDomainConfig.class
})
public class LibraryHexagonalApplication {

	public static void main(String[] args) {
		SpringApplication.run(LibraryHexagonalApplication.class, args);
	}
}
