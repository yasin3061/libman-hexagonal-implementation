package com.yasinbee.libman.hex.infrastructure;

import com.yasinbee.libman.hex.domain.email.core.EmailFacade;
import com.yasinbee.libman.hex.domain.email.core.ports.incoming.SendReservationConfirmation;
import com.yasinbee.libman.hex.domain.email.core.ports.outgoing.EmailDatabase;
import com.yasinbee.libman.hex.domain.email.core.ports.outgoing.EmailSender;
import com.yasinbee.libman.hex.domain.email.infrastructure.EmailDatabaseAdapter;
import com.yasinbee.libman.hex.domain.email.infrastructure.SendGridEmailSender;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.core.JdbcTemplate;

public class EmailDomainConfig {

    @Bean
    public EmailSender emailSender() {
        return new SendGridEmailSender();
    }

    @Bean
    public EmailDatabase libraryDatabase(JdbcTemplate jdbcTemplate) {
        return new EmailDatabaseAdapter(jdbcTemplate);
    }

    @Bean
    public SendReservationConfirmation sendReservationConfirmation(EmailSender emailSender,
                                                                   EmailDatabase database) {
        return new EmailFacade(emailSender, database);
    }
}
