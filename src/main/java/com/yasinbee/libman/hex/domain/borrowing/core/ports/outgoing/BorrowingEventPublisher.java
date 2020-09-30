package com.yasinbee.libman.hex.domain.borrowing.core.ports.outgoing;

import com.yasinbee.libman.hex.domain.borrowing.core.model.BookReservedEvent;

public interface BorrowingEventPublisher {
    public void publish(BookReservedEvent event);
}
