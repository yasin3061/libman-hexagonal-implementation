package com.yasinbee.libman.hex.domain.borrowing.core.ports.incoming;

import com.yasinbee.libman.hex.domain.borrowing.core.model.GiveBackBookCommand;

public interface GiveBackBook {
    void handle(GiveBackBookCommand command);
}
