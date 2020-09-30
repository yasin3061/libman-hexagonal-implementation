package com.yasinbee.libman.hex.domain.user;

import com.yasinbee.libman.hex.domain.user.core.model.User;
import com.yasinbee.libman.hex.domain.user.core.model.UserIdentifier;
import com.yasinbee.libman.hex.domain.user.core.ports.outgoing.UserDatabase;
import org.apache.commons.lang3.reflect.FieldUtils;

import java.util.concurrent.ConcurrentHashMap;

public class InMemoryUserDatabase implements UserDatabase {

    ConcurrentHashMap<Long, User> users = new ConcurrentHashMap<>();

    @Override
    public UserIdentifier save(User user) {
        Long id = users.size() + 1L;

        try {
            FieldUtils.writeField(user, "id", id, true);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        users.put(id, user);
        return new UserIdentifier(id);
    }
}
