package com.yasinbee.libman.hex.domain.user.infrastructure;

import com.yasinbee.libman.hex.domain.user.core.model.User;
import com.yasinbee.libman.hex.domain.user.core.model.UserIdentifier;
import com.yasinbee.libman.hex.domain.user.core.ports.outgoing.UserDatabase;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class UserDatabaseAdapter implements UserDatabase {

    private final UserRepository userRepository;

    @Override
    public UserIdentifier save(User user) {
        User savedUser = userRepository.save(user);
        return new UserIdentifier(savedUser.getIdentifierAsLong());
    }
}
