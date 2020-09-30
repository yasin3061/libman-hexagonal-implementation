package com.yasinbee.libman.hex.domain.inventory.core.ports.outgoing;

import com.yasinbee.libman.hex.domain.inventory.core.model.NewBookWasAddedEvent;

public interface InventoryEventPublisher {
    void publishNewBookWasAddedEvent(NewBookWasAddedEvent event);
}
