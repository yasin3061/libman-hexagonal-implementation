package com.yasinbee.libman.hex.domain.borrowing.core.ports.incoming;

import com.yasinbee.libman.hex.domain.borrowing.core.model.BookReservationCommand;

public interface ReserveBook {
    Long handle(BookReservationCommand bookReservation);
}
