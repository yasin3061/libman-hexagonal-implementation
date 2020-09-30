package com.yasinbee.libman.hex.domain.inventory.infrastructure;

import com.yasinbee.libman.hex.domain.inventory.core.model.NewBookWasAddedEvent;
import com.yasinbee.libman.hex.domain.inventory.core.ports.outgoing.InventoryEventPublisher;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;

@RequiredArgsConstructor
public class SpringInventoryEventPublisherAdapter implements InventoryEventPublisher {

    private final ApplicationEventPublisher applicationEventPublisher;

    @Override
    public void publishNewBookWasAddedEvent(NewBookWasAddedEvent event) {
        applicationEventPublisher.publishEvent(event);
    }
}
