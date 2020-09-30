package com.yasinbee.libman.hex.infrastructure;

import com.yasinbee.libman.hex.domain.inventory.core.InventoryFacade;
import com.yasinbee.libman.hex.domain.inventory.infrastructure.BookRepository;
import com.yasinbee.libman.hex.domain.inventory.infrastructure.GoogleBooksAdapter;
import com.yasinbee.libman.hex.domain.inventory.infrastructure.InventoryDatabaseAdapter;
import com.yasinbee.libman.hex.domain.inventory.infrastructure.SpringInventoryEventPublisherAdapter;
import com.yasinbee.libman.hex.domain.inventory.core.ports.incoming.AddNewBook;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

public class InventoryDomainConfig {

    @Bean
    SpringInventoryEventPublisherAdapter springInventoryEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
        return new SpringInventoryEventPublisherAdapter(applicationEventPublisher);
    }

    @Bean
    AddNewBook addNewBook(BookRepository repository, RestTemplate restTemplate, ApplicationEventPublisher applicationEventPublisher){
        return new InventoryFacade(
                new InventoryDatabaseAdapter(repository),
                new GoogleBooksAdapter(restTemplate),
                springInventoryEventPublisher(applicationEventPublisher));
    }
}
