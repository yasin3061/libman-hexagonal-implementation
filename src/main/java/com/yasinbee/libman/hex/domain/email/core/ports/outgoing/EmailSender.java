package com.yasinbee.libman.hex.domain.email.core.ports.outgoing;


import com.yasinbee.libman.hex.domain.email.core.model.ReservationConfirmEmail;

public interface EmailSender {
    void sendReservationConfirmationEmail(ReservationConfirmEmail reservationConfirmEmail);
}
