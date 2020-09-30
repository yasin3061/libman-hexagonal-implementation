package com.yasinbee.libman.hex.domain.borrowing.infrastructure;

import com.yasinbee.libman.hex.domain.borrowing.core.model.BookReservedEvent;
import com.yasinbee.libman.hex.domain.borrowing.core.ports.outgoing.BorrowingEventPublisher;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SpringBorrowingEventPublisherAdapter implements BorrowingEventPublisher {

    private final ApplicationEventPublisher eventPublisher;

    @Override
    public void publish(BookReservedEvent event) {
        eventPublisher.publishEvent(event);
    }
}
