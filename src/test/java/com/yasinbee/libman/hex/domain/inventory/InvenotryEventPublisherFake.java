package com.yasinbee.libman.hex.domain.inventory;

import com.yasinbee.libman.hex.domain.inventory.core.model.NewBookWasAddedEvent;
import com.yasinbee.libman.hex.domain.inventory.core.ports.outgoing.InventoryEventPublisher;

public class InvenotryEventPublisherFake implements InventoryEventPublisher {

    @Override
    public void publishNewBookWasAddedEvent(NewBookWasAddedEvent event) { }
}
