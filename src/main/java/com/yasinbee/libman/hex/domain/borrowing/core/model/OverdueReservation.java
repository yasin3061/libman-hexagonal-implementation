package com.yasinbee.libman.hex.domain.borrowing.core.model;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class OverdueReservation {
    private Long reservationId;
    private Long bookIdentification;

    public Long getBookIdentificationAsLong() {
        return bookIdentification;
    }
}
