package com.yasinbee.libman.hex.domain.borrowing;

import com.yasinbee.libman.hex.domain.borrowing.core.model.ActiveUser;
import com.yasinbee.libman.hex.domain.borrowing.core.model.AvailableBook;
import com.yasinbee.libman.hex.domain.borrowing.core.model.BorrowedBook;
import com.yasinbee.libman.hex.domain.borrowing.core.model.OverdueReservation;
import com.yasinbee.libman.hex.domain.borrowing.core.model.ReservationDetails;
import com.yasinbee.libman.hex.domain.borrowing.core.model.ReservationId;
import com.yasinbee.libman.hex.domain.borrowing.core.model.ReservedBook;
import com.yasinbee.libman.hex.domain.borrowing.core.ports.outgoing.BorrowingDatabase;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class InMemoryBorrowingDatabase implements BorrowingDatabase {

    ConcurrentHashMap<Long, ActiveUser> activeUsers = new ConcurrentHashMap<>();
    ConcurrentHashMap<Long, AvailableBook> availableBooks = new ConcurrentHashMap<>();
    ConcurrentHashMap<Long, ReservedBook> reservedBooks = new ConcurrentHashMap<>();
    ConcurrentHashMap<Long, BorrowedBook> borrowedBooks = new ConcurrentHashMap<>();

    @Override
    public void save(AvailableBook availableBook) {
        availableBooks.put(availableBook.getIdAsLong(), availableBook);
        reservedBooks.remove(availableBook.getIdAsLong());
        borrowedBooks.remove(availableBook.getIdAsLong());
    }

    @Override
    public ReservationDetails save(ReservedBook reservedBook) {
        Long reservationId = new Random().nextLong();
        availableBooks.remove(reservedBook.getIdAsLong());
        reservedBooks.put(reservationId, reservedBook);
        return new ReservationDetails(new ReservationId(reservationId), reservedBook);
    }

    @Override
    public void save(BorrowedBook borrowedBook) {
        reservedBooks.remove(borrowedBook.getIdAsLong());
        borrowedBooks.put(borrowedBook.getIdAsLong(), borrowedBook);
    }

    @Override
    public Optional<AvailableBook> getAvailableBook(Long bookId) {
        if (availableBooks.containsKey(bookId)) {
            return Optional.of(availableBooks.get(bookId));
        } else {
            return Optional.empty();
        }
    }

    @Override
    public Optional<ActiveUser> getActiveUser(Long userId) {
        if (activeUsers.containsKey(userId)) {
            return Optional.of(activeUsers.get(userId));
        } else {
            return Optional.empty();
        }
    }

    @Override
    public List<OverdueReservation> findReservationsForMoreThan(Long days) {
        return reservedBooks.values().stream()
                .filter(reservedBook ->
                        Instant.now().isAfter(
                                reservedBook.getReservedDateAsInstant().plus(days,
                                        ChronoUnit.DAYS)))
                .map(reservedBook ->
                        new OverdueReservation(
                                1L,
                                reservedBook.getIdAsLong()))
                .collect(Collectors.toList());
    }

    @Override
    public Optional<ReservedBook> getReservedBook(Long bookId) {
        return Optional.of(reservedBooks.get(bookId));
    }

    @Override
    public Optional<BorrowedBook> getBorrowedBook(Long bookId) {
        return Optional.of(borrowedBooks.get(bookId));
    }
}
