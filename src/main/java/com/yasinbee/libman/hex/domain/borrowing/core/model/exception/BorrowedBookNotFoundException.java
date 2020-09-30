package com.yasinbee.libman.hex.domain.borrowing.core.model.exception;

public class BorrowedBookNotFoundException extends RuntimeException {
    public BorrowedBookNotFoundException(Long bookId) {
        super("There is no borrowed book with an ID: " + bookId,
                null,
                false,
                false);
    }
}
