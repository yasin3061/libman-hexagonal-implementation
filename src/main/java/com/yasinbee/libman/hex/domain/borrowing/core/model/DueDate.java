package com.yasinbee.libman.hex.domain.borrowing.core.model;

import lombok.AllArgsConstructor;

import java.time.Instant;

@AllArgsConstructor
public class DueDate {
    private final Instant timeStamp;

    public Instant asInstant(){
        return timeStamp;
    }
}
