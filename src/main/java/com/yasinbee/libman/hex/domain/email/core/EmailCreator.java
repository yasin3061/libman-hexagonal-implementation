package com.yasinbee.libman.hex.domain.email.core;

import com.yasinbee.libman.hex.domain.email.core.model.EmailAddress;
import com.yasinbee.libman.hex.domain.email.core.model.ReservationConfirmEmail;

class EmailCreator {

    static ReservationConfirmEmail reservationEmail(Long reservationId, String bookTitle, String emailTo){

        EmailAddress from = new EmailAddress("tom@library.com");
        EmailAddress to = new EmailAddress(emailTo);

        String subject = String.format("Library - book reservation confirmation (id - %d)", reservationId);
        String content = String.format("Dear reader,%n you have reserved a %s book which will be waiting for you in our library for next 2 days. Your reservation id is %d. %n Have a nice day, %n Library",
                bookTitle, reservationId);
        return new ReservationConfirmEmail(from, to, subject, content);
    }
}
