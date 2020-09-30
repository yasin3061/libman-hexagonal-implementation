package com.yasinbee.libman.hex.domain.inventory.core.ports.outgoing;

import com.yasinbee.libman.hex.domain.inventory.core.model.Book;

public interface InventoryDatabase {
    Book save(Book book);
}
