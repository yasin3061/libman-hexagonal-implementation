package com.yasinbee.libman.hex.domain.email.application;

import com.yasinbee.libman.hex.domain.borrowing.core.model.BookReservedEvent;
import com.yasinbee.libman.hex.domain.email.core.model.SendReservationConfirmationCommand;
import com.yasinbee.libman.hex.domain.email.core.ports.incoming.SendReservationConfirmation;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class BookReservedEventHandler {

    private final SendReservationConfirmation sendReservationConfirmation;

    @EventListener
    public void handle(BookReservedEvent event) {
        sendReservationConfirmation.handle(
                new SendReservationConfirmationCommand(
                        event.getReservationIdAsLong(),
                        event.getUserIdAsLong(),
                        event.getBookIdAsLong()));
    }
}
