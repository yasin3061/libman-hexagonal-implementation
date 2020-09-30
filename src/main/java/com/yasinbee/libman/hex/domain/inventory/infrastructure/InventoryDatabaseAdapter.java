package com.yasinbee.libman.hex.domain.inventory.infrastructure;

import com.yasinbee.libman.hex.domain.inventory.core.model.Book;
import com.yasinbee.libman.hex.domain.inventory.core.ports.outgoing.InventoryDatabase;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class InventoryDatabaseAdapter implements InventoryDatabase {

    private final BookRepository repository;

    @Override
    public Book save(Book book) {
        return repository.save(book);
    }
}
