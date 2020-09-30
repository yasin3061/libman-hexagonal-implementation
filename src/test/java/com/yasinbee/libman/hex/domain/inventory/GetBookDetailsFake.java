package com.yasinbee.libman.hex.domain.inventory;

import com.yasinbee.libman.hex.BookTestData;
import com.yasinbee.libman.hex.domain.inventory.core.model.Book;
import com.yasinbee.libman.hex.domain.inventory.core.ports.outgoing.GetBookDetails;

import java.util.HashMap;
import java.util.Map;

public class GetBookDetailsFake implements GetBookDetails {

    private Map<String, Book> books;

    public GetBookDetailsFake() {
        books = new HashMap<String, Book>();
        books.put(
                BookTestData.homoDeusBookGoogleId(),
                BookTestData.homoDeusBook());
    }

    @Override
    public Book handle(String bookId) {
        return books.get(bookId);
    }
}
