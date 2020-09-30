package com.yasinbee.libman.hex.domain.borrowing.core;

import com.yasinbee.libman.hex.domain.borrowing.core.model.ActiveUser;
import com.yasinbee.libman.hex.domain.borrowing.core.model.AvailableBook;
import com.yasinbee.libman.hex.domain.borrowing.core.model.BookReservationCommand;
import com.yasinbee.libman.hex.domain.borrowing.core.model.BookReservedEvent;
import com.yasinbee.libman.hex.domain.borrowing.core.model.BorrowBookCommand;
import com.yasinbee.libman.hex.domain.borrowing.core.model.BorrowedBook;
import com.yasinbee.libman.hex.domain.borrowing.core.model.GiveBackBookCommand;
import com.yasinbee.libman.hex.domain.borrowing.core.model.MakeBookAvailableCommand;
import com.yasinbee.libman.hex.domain.borrowing.core.model.OverdueReservation;
import com.yasinbee.libman.hex.domain.borrowing.core.model.ReservationDetails;
import com.yasinbee.libman.hex.domain.borrowing.core.model.ReservedBook;
import com.yasinbee.libman.hex.domain.borrowing.core.model.exception.ActiveUserNotFoundException;
import com.yasinbee.libman.hex.domain.borrowing.core.model.exception.AvailableBookNotFoundExeption;
import com.yasinbee.libman.hex.domain.borrowing.core.model.exception.BorrowedBookNotFoundException;
import com.yasinbee.libman.hex.domain.borrowing.core.model.exception.ReservedBookNotFoundException;
import com.yasinbee.libman.hex.domain.borrowing.core.ports.incoming.BorrowBook;
import com.yasinbee.libman.hex.domain.borrowing.core.ports.incoming.CancelOverdueReservations;
import com.yasinbee.libman.hex.domain.borrowing.core.ports.incoming.GiveBackBook;
import com.yasinbee.libman.hex.domain.borrowing.core.ports.incoming.MakeBookAvailable;
import com.yasinbee.libman.hex.domain.borrowing.core.ports.incoming.ReserveBook;
import com.yasinbee.libman.hex.domain.borrowing.core.ports.outgoing.BorrowingDatabase;
import com.yasinbee.libman.hex.domain.borrowing.core.ports.outgoing.BorrowingEventPublisher;

import java.util.List;

public class BorrowingFacade implements MakeBookAvailable, ReserveBook, CancelOverdueReservations, BorrowBook, GiveBackBook {

    private final BorrowingDatabase database;
    private final BorrowingEventPublisher eventPublisher;

    public BorrowingFacade(BorrowingDatabase database, BorrowingEventPublisher eventPublisher) {
        this.database = database;
        this.eventPublisher = eventPublisher;
    }

    @Override
    public void handle(MakeBookAvailableCommand bookAvailableCommand) {
        database.save(new AvailableBook(bookAvailableCommand.getBookId()));
    }

    @Override
    public Long handle(BookReservationCommand bookReservation) {
        AvailableBook availableBook =
                database.getAvailableBook(bookReservation.getBookId())
                .orElseThrow(() -> new AvailableBookNotFoundExeption(bookReservation.getBookId()));

        ActiveUser activeUser =
                database.getActiveUser(bookReservation.getUserId())
                .orElseThrow(() -> new ActiveUserNotFoundException(bookReservation.getUserId()));

        ReservedBook reservedBook = activeUser.reserve(availableBook);
        ReservationDetails reservationDetails = database.save(reservedBook);
        eventPublisher.publish(new BookReservedEvent(reservationDetails));
        return reservationDetails.getReservationId().getIdAsLong();
    }

    @Override
    public void cancelOverdueReservations() {
        List<OverdueReservation> overdueReservationList = database.findReservationsForMoreThan(3L);
        overdueReservationList.forEach(
                overdueBook -> database.save(new AvailableBook(overdueBook.getBookIdentificationAsLong())));
    }

    @Override
    public void handle(BorrowBookCommand borrowBookCommand) {
        ActiveUser activeUser =
                database.getActiveUser(borrowBookCommand.getUserId())
                        .orElseThrow(() -> new ActiveUserNotFoundException(borrowBookCommand.getUserId()));
        ReservedBook reservedBook =
                database.getReservedBook(borrowBookCommand.getBookId())
                .orElseThrow(() -> new ReservedBookNotFoundException(borrowBookCommand.getBookId()));

        BorrowedBook borrowedBook = activeUser.borrow(reservedBook);
        database.save(borrowedBook);
    }

    @Override
    public void handle(GiveBackBookCommand command) {
        BorrowedBook borrowedBook =
                database.getBorrowedBook(command.getBookId())
                .orElseThrow(() -> new BorrowedBookNotFoundException(command.getBookId()));
        ActiveUser activeUser =
                database.getActiveUser(command.getUserId())
                        .orElseThrow(() -> new ActiveUserNotFoundException(command.getUserId()));

        AvailableBook availableBook = activeUser.giveBack(borrowedBook);
        database.save(availableBook);
    }
}
