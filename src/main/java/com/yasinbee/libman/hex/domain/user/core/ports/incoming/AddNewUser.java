package com.yasinbee.libman.hex.domain.user.core.ports.incoming;

import com.yasinbee.libman.hex.domain.user.core.model.AddUserCommand;
import com.yasinbee.libman.hex.domain.user.core.model.UserIdentifier;

public interface AddNewUser {
    UserIdentifier handle(AddUserCommand addUserCommand);
}
