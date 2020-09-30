package com.yasinbee.libman.hex.domain.borrowing.core.ports.incoming;

import com.yasinbee.libman.hex.domain.borrowing.core.model.MakeBookAvailableCommand;

public interface MakeBookAvailable {
    void handle(MakeBookAvailableCommand bookAvailableCommand);
}
