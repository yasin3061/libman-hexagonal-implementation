package com.yasinbee.libman.hex.domain.borrowing.core.model.exception;

public class ReservedBookNotFoundException extends RuntimeException {
    public ReservedBookNotFoundException(Long bookId) {
        super("There is no reserved book with an ID: " + bookId,
                null,
                false,
                false);
    }
}
