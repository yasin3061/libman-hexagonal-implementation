package com.yasinbee.libman.hex.domain.email;

import com.yasinbee.libman.hex.domain.email.core.ports.outgoing.EmailDatabase;

import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public class InMemoryEmailDatabase implements EmailDatabase {

    ConcurrentHashMap<Long, String> bookTitles = new ConcurrentHashMap<>();
    ConcurrentHashMap<Long, String> emailAddresses = new ConcurrentHashMap<>();

    @Override
    public Optional<String> getTitleByBookId(Long bookId) {
        return Optional.of(bookTitles.get(bookId));
    }

    @Override
    public Optional<String> getUserEmailAddress(Long userId) {
        return Optional.of(emailAddresses.get(userId));
    }
}
