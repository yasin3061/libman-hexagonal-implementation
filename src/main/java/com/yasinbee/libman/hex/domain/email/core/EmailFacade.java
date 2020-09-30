package com.yasinbee.libman.hex.domain.email.core;

import com.yasinbee.libman.hex.domain.email.core.ports.incoming.SendReservationConfirmation;
import com.yasinbee.libman.hex.domain.email.core.ports.outgoing.EmailDatabase;
import com.yasinbee.libman.hex.domain.email.core.ports.outgoing.EmailSender;
import com.yasinbee.libman.hex.domain.email.core.model.ReservationConfirmEmail;
import com.yasinbee.libman.hex.domain.email.core.model.SendReservationConfirmationCommand;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class EmailFacade implements SendReservationConfirmation {

    private final EmailSender emailSender;
    private final EmailDatabase database;

    @Override
    public void handle(SendReservationConfirmationCommand sendReservationConfirmation) {
        String bookTitle = database
                .getTitleByBookId(sendReservationConfirmation.getBookId())
                .orElseThrow(() -> new IllegalArgumentException("Can't get book title from database. Reason: there is no book with an id: " + sendReservationConfirmation.getBookId()));
        String userEmailAddress = database
                .getUserEmailAddress(sendReservationConfirmation.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("Can't get email address from database. Reason: there is no user with an id: " + sendReservationConfirmation.getUserId()));

        ReservationConfirmEmail reservationConfirmEmail = EmailCreator.reservationEmail(
                sendReservationConfirmation.getReservationId(),
                bookTitle,
                userEmailAddress
        );
        emailSender.sendReservationConfirmationEmail(reservationConfirmEmail);
    }
}
