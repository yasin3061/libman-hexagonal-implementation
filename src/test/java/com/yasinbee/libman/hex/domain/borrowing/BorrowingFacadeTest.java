package com.yasinbee.libman.hex.domain.borrowing;

import com.yasinbee.libman.hex.domain.borrowing.core.BorrowingFacade;
import com.yasinbee.libman.hex.domain.borrowing.core.model.ActiveUser;
import com.yasinbee.libman.hex.domain.borrowing.core.model.AvailableBook;
import com.yasinbee.libman.hex.domain.borrowing.core.model.BookReservationCommand;
import com.yasinbee.libman.hex.domain.borrowing.core.model.BorrowBookCommand;
import com.yasinbee.libman.hex.domain.borrowing.core.model.BorrowedBook;
import com.yasinbee.libman.hex.domain.borrowing.core.model.GiveBackBookCommand;
import com.yasinbee.libman.hex.domain.borrowing.core.model.MakeBookAvailableCommand;
import com.yasinbee.libman.hex.domain.borrowing.core.model.ReservedBook;
import com.yasinbee.libman.hex.domain.borrowing.core.model.exception.ActiveUserNotFoundException;
import com.yasinbee.libman.hex.domain.borrowing.core.model.exception.AvailableBookNotFoundExeption;
import com.yasinbee.libman.hex.domain.borrowing.core.model.exception.TooManyBooksAssignedToUserException;
import com.yasinbee.libman.hex.domain.borrowing.core.ports.outgoing.BorrowingEventPublisher;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class BorrowingFacadeTest {

    private BorrowingFacade facade;
    private InMemoryBorrowingDatabase database;
    private BorrowingEventPublisher eventPublisher;

    @BeforeEach
    public void init(){
        database = new InMemoryBorrowingDatabase();
        eventPublisher = new BorrowingEventPublisherFake();
        facade = new BorrowingFacade(database, eventPublisher);
    }

    @Test
    @DisplayName("Make book available")
    public void whenMakeBookAvailableCommandReceived_thenBookIsOnAvailableStatus() {
        //given
        MakeBookAvailableCommand makeBookAvailableCommand =
                MakeBookAvailableCommand.builder()
                        .bookId(100L)
                        .build();

        //when
        facade.handle(makeBookAvailableCommand);

        //then
        assertTrue(database.availableBooks.containsKey(100L));
        assertTrue(database.availableBooks.containsValue(new AvailableBook(100L)));
    }

    @Test
    @DisplayName("Make successful book reservation")
    public void givenAvailableBooksAndActiveUser_whenMakingReservation_thenBookIsReserved(){
        //given
        BookReservationCommand reservationCommand = ReservationTestData.anyBookReservationCommand(100L, 100L);
        AvailableBook availableBook = ReservationTestData.anyAvailableBook(reservationCommand.getBookId());
        ActiveUser activeUser = ReservationTestData.anyActiveUser(reservationCommand.getUserId());

        database.activeUsers.put(activeUser.getIdAsLong(), activeUser);
        database.availableBooks.put(availableBook.getIdAsLong(), availableBook);

        //when
        facade.handle(reservationCommand);

        //then
        assertEquals(1, activeUser.getReservedBookList().size());
        assertEquals(availableBook.getIdAsLong(), activeUser.getReservedBookList().get(0).getIdAsLong());
    }

    @Test
    @DisplayName("User can't have more than 3 reservations")
    public void givenActiveUserAlreadyHas3Books_whenMakingReservation_thenBookIsNotReserved(){
        //given
        BookReservationCommand firstReservationCommand = ReservationTestData.anyBookReservationCommand(100L, 100L);
        BookReservationCommand secondReservationCommand = ReservationTestData.anyBookReservationCommand(101L, 100L);
        BookReservationCommand thirdReservationCommand = ReservationTestData.anyBookReservationCommand(102L, 100L);
        BookReservationCommand fourthReservationCommand = ReservationTestData.anyBookReservationCommand(103L, 100L);

        AvailableBook availableBookNo1 = ReservationTestData.anyAvailableBook(firstReservationCommand.getBookId());
        AvailableBook availableBookNo2 = ReservationTestData.anyAvailableBook(secondReservationCommand.getBookId());
        AvailableBook availableBookNo3 = ReservationTestData.anyAvailableBook(thirdReservationCommand.getBookId());
        AvailableBook availableBookNo4 = ReservationTestData.anyAvailableBook(fourthReservationCommand.getBookId());

        ActiveUser activeUser = ReservationTestData.anyActiveUser(firstReservationCommand.getUserId());

        database.availableBooks.put(availableBookNo1.getIdAsLong(), availableBookNo1);
        database.availableBooks.put(availableBookNo2.getIdAsLong(), availableBookNo2);
        database.availableBooks.put(availableBookNo3.getIdAsLong(), availableBookNo3);
        database.availableBooks.put(availableBookNo4.getIdAsLong(), availableBookNo4);
        database.activeUsers.put(activeUser.getIdAsLong(), activeUser);

        facade.handle(firstReservationCommand);
        facade.handle(secondReservationCommand);
        facade.handle(thirdReservationCommand);

        //when & then
        assertThrows(
                TooManyBooksAssignedToUserException.class,
                () -> facade.handle(fourthReservationCommand));
    }

    @Test
    @DisplayName("Try to reserve book,but it's not available")
    public void givenNotAvailableBook_whenMakingReservation_thenThrowException(){
        //given
        BookReservationCommand reservationCommand = ReservationTestData.anyBookReservationCommand(100L, 100L);
        ActiveUser activeUser = ReservationTestData.anyActiveUser(reservationCommand.getUserId());

        database.activeUsers.put(activeUser.getIdAsLong(), activeUser);

        assertThrows(
                AvailableBookNotFoundExeption.class,
                () -> facade.handle(reservationCommand));
    }

    @Test
    @DisplayName("Try to reserve book, but active user is not found")
    public void givenNotActiveUser_whenMakingReservation_thenThrowException(){
        //given
        BookReservationCommand reservationCommand = ReservationTestData.anyBookReservationCommand(100L, 100L);
        AvailableBook availableBook = ReservationTestData.anyAvailableBook(reservationCommand.getBookId());

        database.availableBooks.put(availableBook.getIdAsLong(), availableBook);

        //when & then
        assertThrows(
                ActiveUserNotFoundException.class,
                () -> facade.handle(reservationCommand));
    }

    @Test
    @DisplayName("Cancel reservation after 3 days")
    public void givenBookIsReserved_when3daysPass_thenBookIsAvailable(){
        //given
        ReservedBook reservedBook = ReservationTestData.anyReservedBook(100L, 100L);
        changeReservationTimeFor(reservedBook, Instant.now().minus(4, ChronoUnit.DAYS));
        database.reservedBooks.put(100L, reservedBook);

        //when
        facade.cancelOverdueReservations();

        //then
        assertEquals(0, database.reservedBooks.size());
    }

    @Test
    @DisplayName("Do not cancel reservation after 2 days")
    public void givenBookIsReserved_when2daysPass_thenBookIsStillReserved(){
        //given
        ReservedBook reservedBook = ReservationTestData.anyReservedBook(100L, 100L);
        changeReservationTimeFor(reservedBook, Instant.now().minus(2, ChronoUnit.DAYS));
        database.reservedBooks.put(100L, reservedBook);

        //when
        facade.cancelOverdueReservations();

        //then
        assertEquals(1, database.reservedBooks.size());
    }

    @Test
    @DisplayName("Successfully borrow a book")
    public void givenReservedBookAndActiveUser_whenBorrowing_thenBookIsBorrowed(){
        //given
        BorrowBookCommand borrowBookCommand = BorrowTestData.anyBorrowBookCommand(100L, 100L);
        ReservedBook reservedBook = BorrowTestData.anyReservedBook(borrowBookCommand.getBookId(), borrowBookCommand.getUserId());
        ActiveUser activeUser = BorrowTestData.anyActiveUser(borrowBookCommand.getUserId());

        database.activeUsers.put(activeUser.getIdAsLong(), activeUser);
        database.reservedBooks.put(reservedBook.getIdAsLong(), reservedBook);

        //when
        facade.handle(borrowBookCommand);

        //then
        assertEquals(1, activeUser.getBorrowedBookList().size());
    }

    @Test
    @DisplayName("Successful give back a book")
    public void givenUserWithBorrowedBook_whenBookIsReturned_thenBookIsAvailable(){
        //given
        GiveBackBookCommand giveBackBookCommand = BorrowTestData.anyGiveBookCommand(100L, 100L);
        BorrowedBook borrowedBook = BorrowTestData.anyBorrowedBook(giveBackBookCommand.getBookId(), giveBackBookCommand.getUserId());
        ActiveUser activeUser = BorrowTestData.anyActiveUserWithBorrowedBooks(giveBackBookCommand.getUserId(), new ArrayList<BorrowedBook>(Arrays.asList(borrowedBook)));

        database.borrowedBooks.put(borrowedBook.getIdAsLong(), borrowedBook);
        database.activeUsers.put(activeUser.getIdAsLong(), activeUser);

        //when
        facade.handle(giveBackBookCommand);

        //then
        assertEquals(0, database.borrowedBooks.size());
        assertEquals(1, database.availableBooks.size());
        assertEquals(0, database.activeUsers.get(activeUser.getIdAsLong()).getBorrowedBookList().size());
    }

    private void changeReservationTimeFor(ReservedBook reservedBook, Instant reservationDate) {
        try {
            FieldUtils.writeField(reservedBook, "reservedDate", reservationDate, true);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }
}
