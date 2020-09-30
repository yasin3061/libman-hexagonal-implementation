package com.yasinbee.libman.hex.domain.borrowing.core.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@AllArgsConstructor
@Getter
@Builder
public class GiveBackBookCommand {
    private final Long bookId;
    private final Long userId;
}
