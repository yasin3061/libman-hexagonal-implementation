package com.yasinbee.libman.hex.domain.user.core.ports.outgoing;

import com.yasinbee.libman.hex.domain.user.core.model.User;
import com.yasinbee.libman.hex.domain.user.core.model.UserIdentifier;

public interface UserDatabase {
    UserIdentifier save(User user);
}
