package com.yasinbee.libman.hex.domain.inventory;

import com.yasinbee.libman.hex.BookTestData;
import com.yasinbee.libman.hex.domain.inventory.core.InventoryFacade;
import com.yasinbee.libman.hex.domain.inventory.core.model.AddNewBookCommand;
import com.yasinbee.libman.hex.domain.inventory.core.model.Book;
import com.yasinbee.libman.hex.domain.inventory.core.ports.outgoing.GetBookDetails;
import com.yasinbee.libman.hex.domain.inventory.core.ports.outgoing.InventoryEventPublisher;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;


public class InventoryFacadeTest {

    private GetBookDetails getBookDetails;
    private InMemoryInventoryDatabase database;
    private InventoryEventPublisher eventPublisher;
    private InventoryFacade facade;

    @BeforeEach
    public void init() {
        database = new InMemoryInventoryDatabase();
        getBookDetails = new GetBookDetailsFake();
        eventPublisher = new InvenotryEventPublisherFake();
        facade = new InventoryFacade(database, getBookDetails, eventPublisher);
    }

    @Test
    @DisplayName("Correctly save a new book in a repository")
    public void correctlySaveBook() {
        //given
        AddNewBookCommand externalBookId = AddNewBookCommand
                .builder()
                .googleBookId(BookTestData.homoDeusBookGoogleId())
                .build();

        //when
        facade.handle(externalBookId);

        //then
        Book actualBook = database.books.get(1L);
        assertNotNull(actualBook);
    }
}
