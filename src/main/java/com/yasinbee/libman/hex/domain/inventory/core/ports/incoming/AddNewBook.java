package com.yasinbee.libman.hex.domain.inventory.core.ports.incoming;

import com.yasinbee.libman.hex.domain.inventory.core.model.AddNewBookCommand;

public interface AddNewBook {
    void handle(AddNewBookCommand addNewBookCommand);
}
