package com.yasinbee.libman.hex.domain.user.core;

import com.yasinbee.libman.hex.domain.user.core.model.EmailAddress;
import com.yasinbee.libman.hex.domain.user.core.model.User;
import com.yasinbee.libman.hex.domain.user.core.model.UserIdentifier;
import com.yasinbee.libman.hex.domain.user.core.ports.incoming.AddNewUser;
import com.yasinbee.libman.hex.domain.user.core.ports.outgoing.UserDatabase;
import com.yasinbee.libman.hex.domain.user.core.model.AddUserCommand;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class UserFacade implements AddNewUser {

    private final UserDatabase database;

    @Override
    public UserIdentifier handle(AddUserCommand addUserCommand) {
        User user = new User(
                new EmailAddress(addUserCommand.getEmail()),
                addUserCommand.getFirstName(),
                addUserCommand.getLastName()
        );
        return database.save(user);
    }
}
