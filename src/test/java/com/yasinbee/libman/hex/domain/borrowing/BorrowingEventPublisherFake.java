package com.yasinbee.libman.hex.domain.borrowing;

import com.yasinbee.libman.hex.domain.borrowing.core.model.BookReservedEvent;
import com.yasinbee.libman.hex.domain.borrowing.core.ports.outgoing.BorrowingEventPublisher;

public class BorrowingEventPublisherFake implements BorrowingEventPublisher {

    @Override
    public void publish(BookReservedEvent event) {

    }
}
