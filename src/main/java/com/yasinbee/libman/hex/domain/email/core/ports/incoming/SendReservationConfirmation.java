package com.yasinbee.libman.hex.domain.email.core.ports.incoming;

import com.yasinbee.libman.hex.domain.email.core.model.SendReservationConfirmationCommand;

public interface SendReservationConfirmation {
    void handle(SendReservationConfirmationCommand reservationConfirmationCommand);
}
