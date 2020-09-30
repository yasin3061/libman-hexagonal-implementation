package com.yasinbee.libman.hex.domain.email.core.model;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EmailAddress {

    private final String value;

    public EmailAddress(String value) {
        Pattern pattern = Pattern.compile("^(.+)@(.+)$");
        Matcher matcher = pattern.matcher(value);
        if (matcher.matches()) {
            this.value = value;
        } else {
            throw new IllegalArgumentException("Provided value is not an email address");
        }

    }

    public String getAsString() {
        return value;
    }
}
