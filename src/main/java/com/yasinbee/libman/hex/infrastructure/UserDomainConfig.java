package com.yasinbee.libman.hex.infrastructure;

import com.yasinbee.libman.hex.domain.user.core.UserFacade;
import com.yasinbee.libman.hex.domain.user.infrastructure.UserDatabaseAdapter;
import com.yasinbee.libman.hex.domain.user.infrastructure.UserRepository;
import com.yasinbee.libman.hex.domain.user.core.ports.incoming.AddNewUser;
import com.yasinbee.libman.hex.domain.user.core.ports.outgoing.UserDatabase;
import org.springframework.context.annotation.Bean;

public class UserDomainConfig {

    @Bean
    public UserDatabase userDatabase(UserRepository userRepository){
        return new UserDatabaseAdapter(userRepository);
    }

    @Bean
    public AddNewUser addNewUser(UserDatabase userDatabase){
        return new UserFacade(userDatabase);
    }
}
