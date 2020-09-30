package com.yasinbee.libman.hex.domain.borrowing.core.model;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class OverdueReservation {
    private final Long reservationId;
    private final Long bookIdentification;

    public Long getBookIdentificationAsLong() {
        return bookIdentification;
    }
}
