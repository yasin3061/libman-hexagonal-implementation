package com.yasinbee.libman.hex.domain.inventory.core;

import com.yasinbee.libman.hex.domain.inventory.core.model.AddNewBookCommand;
import com.yasinbee.libman.hex.domain.inventory.core.model.Book;
import com.yasinbee.libman.hex.domain.inventory.core.model.NewBookWasAddedEvent;
import com.yasinbee.libman.hex.domain.inventory.core.ports.incoming.AddNewBook;
import com.yasinbee.libman.hex.domain.inventory.core.ports.outgoing.GetBookDetails;
import com.yasinbee.libman.hex.domain.inventory.core.ports.outgoing.InventoryDatabase;
import com.yasinbee.libman.hex.domain.inventory.core.ports.outgoing.InventoryEventPublisher;


public class InventoryFacade implements AddNewBook {

    private InventoryDatabase database;
    private GetBookDetails getBookDetails;
    private InventoryEventPublisher eventPublisher;

    public InventoryFacade(InventoryDatabase database, GetBookDetails getBookDetails, InventoryEventPublisher eventPublisher) {
        this.database = database;
        this.getBookDetails = getBookDetails;
        this.eventPublisher = eventPublisher;
    }

    @Override
    public void handle(AddNewBookCommand addNewBookCommand){
        Book book = getBookDetails.handle(addNewBookCommand.getGoogleBookId());
        Book savedBook = database.save(book);
        eventPublisher.publishNewBookWasAddedEvent(new NewBookWasAddedEvent(savedBook.getIdAsLong()));
    }
}
