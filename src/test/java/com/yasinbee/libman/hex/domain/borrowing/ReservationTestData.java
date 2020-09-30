package com.yasinbee.libman.hex.domain.borrowing;

import com.yasinbee.libman.hex.domain.borrowing.core.model.ActiveUser;
import com.yasinbee.libman.hex.domain.borrowing.core.model.AvailableBook;
import com.yasinbee.libman.hex.domain.borrowing.core.model.BookReservationCommand;
import com.yasinbee.libman.hex.domain.borrowing.core.model.BorrowedBook;
import com.yasinbee.libman.hex.domain.borrowing.core.model.ReservedBook;

import java.util.ArrayList;

public class ReservationTestData {

    public static BookReservationCommand anyBookReservationCommand(Long bookId, Long userId) {
        return BookReservationCommand.builder()
                .bookId(bookId)
                .userId(userId)
                .build();
    }

    public static ReservedBook anyReservedBook(Long bookId, Long userId) {
        return new ReservedBook(bookId, userId);
    }

    public static AvailableBook anyAvailableBook(Long bookId) {
        return new AvailableBook(bookId);
    }

    public static ActiveUser anyActiveUser(Long userId) {
        return new ActiveUser(userId, new ArrayList<ReservedBook>(), new ArrayList<BorrowedBook>());
    }
}
