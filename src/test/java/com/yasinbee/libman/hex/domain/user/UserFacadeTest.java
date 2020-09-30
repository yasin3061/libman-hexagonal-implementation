package com.yasinbee.libman.hex.domain.user;

import com.yasinbee.libman.hex.UserTestData;
import com.yasinbee.libman.hex.domain.user.core.UserFacade;
import com.yasinbee.libman.hex.domain.user.core.model.AddUserCommand;
import com.yasinbee.libman.hex.domain.user.core.model.EmailAddress;
import com.yasinbee.libman.hex.domain.user.core.model.User;
import com.yasinbee.libman.hex.domain.user.core.model.UserIdentifier;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class UserFacadeTest {

    private InMemoryUserDatabase database;
    private UserFacade facade;

    @BeforeEach
    public void init(){
        database = new InMemoryUserDatabase();
        facade = new UserFacade(database);
    }

    @Test
    @DisplayName("Add new user")
    public void shouldAddNewUser(){
        //given
        User expectedUser = new User(
                new EmailAddress(UserTestData.johnDoeEmail()),
                "John",
                "Doe"
        );

        AddUserCommand addUserCommand = AddUserCommand.builder()
                .email(UserTestData.johnDoeEmail())
                .firstName("John")
                .lastName("Doe")
                .build();

        //when
        UserIdentifier userIdentifier = facade.handle(addUserCommand);

        //then
        assertTrue(userIdentifier.getAsLong() > 0);
        assertEquals(expectedUser, database.users.get(userIdentifier.getAsLong()));
    }
}
