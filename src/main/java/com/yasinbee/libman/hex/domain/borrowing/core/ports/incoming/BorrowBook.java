package com.yasinbee.libman.hex.domain.borrowing.core.ports.incoming;

import com.yasinbee.libman.hex.domain.borrowing.core.model.BorrowBookCommand;

public interface BorrowBook {
    void handle(BorrowBookCommand borrowBookCommand);
}
