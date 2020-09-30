package com.yasinbee.libman.hex.domain.email;

import com.yasinbee.libman.hex.domain.email.core.model.ReservationConfirmEmail;
import com.yasinbee.libman.hex.domain.email.core.ports.outgoing.EmailSender;

public class EmailSenderFake implements EmailSender {

    @Override
    public void sendReservationConfirmationEmail(ReservationConfirmEmail reservationConfirmEmail) {

    }
}
