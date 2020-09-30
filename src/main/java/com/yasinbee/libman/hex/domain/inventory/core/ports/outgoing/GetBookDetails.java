package com.yasinbee.libman.hex.domain.inventory.core.ports.outgoing;

import com.yasinbee.libman.hex.domain.inventory.core.model.Book;

public interface GetBookDetails {
    Book handle(String bookId);
}
