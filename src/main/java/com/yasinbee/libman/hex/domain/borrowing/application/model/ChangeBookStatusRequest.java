package com.yasinbee.libman.hex.domain.borrowing.application.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ChangeBookStatusRequest {

    private BookStatus status;
    private Long userId;
}
