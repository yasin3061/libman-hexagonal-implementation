package com.yasinbee.libman.hex.domain.borrowing.application;

import com.yasinbee.libman.hex.domain.borrowing.core.ports.incoming.CancelOverdueReservations;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Scheduled;

@RequiredArgsConstructor
public class OverdueReservationScheduler {

    @Qualifier("CancelOverdueReservations")
    private final CancelOverdueReservations overdueReservations;

    @Scheduled(fixedRate = 60 * 1000)
    public void checkOverdueReservations(){
        overdueReservations.cancelOverdueReservations();
    }
}
