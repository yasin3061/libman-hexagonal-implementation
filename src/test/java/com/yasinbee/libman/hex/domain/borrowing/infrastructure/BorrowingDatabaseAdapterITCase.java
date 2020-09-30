package com.yasinbee.libman.hex.domain.borrowing.infrastructure;

import com.yasinbee.libman.hex.DatabaseHelper;
import com.yasinbee.libman.hex.domain.borrowing.core.model.ActiveUser;
import com.yasinbee.libman.hex.domain.borrowing.core.model.AvailableBook;
import com.yasinbee.libman.hex.domain.borrowing.core.model.BorrowedBook;
import com.yasinbee.libman.hex.domain.borrowing.core.model.OverdueReservation;
import com.yasinbee.libman.hex.domain.borrowing.core.model.ReservationDetails;
import com.yasinbee.libman.hex.domain.borrowing.core.model.ReservedBook;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.jdbc.Sql;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.AFTER_TEST_METHOD;

@SpringBootTest
public class BorrowingDatabaseAdapterITCase {

    @Autowired
    private JdbcTemplate jdbcTemplate;
    private DatabaseHelper databaseHelper;
    private BorrowingDatabaseAdapter database;

    @BeforeEach
    public void init(){
        database = new BorrowingDatabaseAdapter(jdbcTemplate);
        databaseHelper = new DatabaseHelper(jdbcTemplate);
    }

    @Test
    @DisplayName("Save book as available")
    @Sql("/book-and-user.sql")
    @Sql(scripts = "/clean-database.sql", executionPhase = AFTER_TEST_METHOD)
    public void shouldSaveAvailableBook(){
        //given
        Long bookId = databaseHelper.getHomoDeusBookId();

        //when
        database.save(new AvailableBook(bookId));

        //then
        Long id = databaseHelper.getPrimaryKeyOfAvailableByBookBy(bookId);
        assertTrue(id > 0);
    }

    @Test
    @DisplayName("Get available book by id")
    @Sql({"/book-and-user.sql", "/available-book.sql"})
    @Sql(scripts = "/clean-database.sql", executionPhase = AFTER_TEST_METHOD)
    public void shouldGetAvailableBook(){
        //given
        Long bookId = databaseHelper.getHomoDeusBookId();

        //when
        Optional<AvailableBook> availableBookOptional = database.getAvailableBook(bookId);

        //then
        assertTrue(availableBookOptional.isPresent());
        assertEquals(bookId, availableBookOptional.get().getIdAsLong());
    }

    @Test
    @DisplayName("Get active user by id")
    @Sql("/book-and-user.sql")
    @Sql(scripts = "/clean-database.sql", executionPhase = AFTER_TEST_METHOD)
    public void shouldGetActiveUser() {
        //given
        Long activeUserId = databaseHelper.getJohnDoeUserId();

        //when
        Optional<ActiveUser> activeUserOptional = database.getActiveUser(activeUserId);

        //then
        assertTrue(activeUserOptional.isPresent());
        assertEquals(activeUserId, activeUserOptional.get().getIdAsLong());
    }

    @Test
    @DisplayName("Save reserved book")
    @Sql({"/book-and-user.sql", "/available-book.sql"})
    @Sql(scripts = "/clean-database.sql", executionPhase = AFTER_TEST_METHOD)
    public void shouldSaveReservedBook(){
        //given
        Long bookId = databaseHelper.getHomoDeusBookId();
        Long activeUserId = databaseHelper.getJohnDoeUserId();

        ReservedBook reservedBook = new ReservedBook(bookId, activeUserId);

        //when
        ReservationDetails reservationDetails = database.save(reservedBook);

        //then
        assertEquals(bookId, reservationDetails.getReservedBook().getIdAsLong());
        assertEquals(activeUserId, reservationDetails.getReservedBook().getAssignedUserIdAsLong());
        assertTrue(reservationDetails.getReservationId().getIdAsLong() > 0);
    }

    @Test
    @DisplayName("Get reserved book by its id")
    @Sql({"/book-and-user.sql", "/reserved-book.sql"})
    @Sql(scripts = "/clean-database.sql", executionPhase = AFTER_TEST_METHOD)
    public void shouldFindReservedBook(){
        //given
        Long bookId = databaseHelper.getHomoDeusBookId();

        //when
        Optional<ReservedBook> reservedBook = database.getReservedBook(bookId);

        //then
        assertTrue(reservedBook.isPresent());
        assertEquals(bookId, reservedBook.get().getIdAsLong());
    }

    @Test
    @DisplayName("Save borrowed book")
    @Sql({"/book-and-user.sql"})
    @Sql(scripts = "/clean-database.sql", executionPhase = AFTER_TEST_METHOD)
    public void shouldSaveBorrowedBook(){
        //given
        Long bookId = databaseHelper.getHomoDeusBookId();
        Long activeUserId = databaseHelper.getJohnDoeUserId();

        BorrowedBook borrowedBook = new BorrowedBook(bookId, activeUserId);

        //when
        database.save(borrowedBook);

        //then
        Long savedBookId = databaseHelper.getPrimaryKeyOfBorrowedByBookId(bookId);
        assertEquals(bookId, savedBookId);
    }

    @Test
    @DisplayName("Find book after 3 days of reservation")
    @Sql({"/book-and-user.sql"})
    @Sql(scripts = "/clean-database.sql", executionPhase = AFTER_TEST_METHOD)
    public void shouldFindOverdueReservations(){
        //given
        Long overdueBookId = databaseHelper.getHomoDeusBookId();
        Long johnDoeUserId = databaseHelper.getJohnDoeUserId();
        jdbcTemplate.update(
                "INSERT INTO public.reserved (book_id, user_id, reserved_date) VALUES (?, ?, ?)",
                overdueBookId,
                johnDoeUserId,
                Instant.now().plus(4, ChronoUnit.DAYS));

        //when
        OverdueReservation overdueReservation = database.findReservationsForMoreThan(3L).get(0);

        //then
        assertEquals(overdueBookId, overdueReservation.getBookIdentificationAsLong());
    }

    @Test
    @DisplayName("Find borrowed book by id")
    @Sql({"/book-and-user.sql", "/borrowed-book.sql"})
    @Sql(scripts = "/clean-database.sql", executionPhase = AFTER_TEST_METHOD)
    public void shouldFindBorrowedBook(){
        //given
        Long bookId = databaseHelper.getHomoDeusBookId();

        //when
        Optional<BorrowedBook> borrowedBook = database.getBorrowedBook(bookId);

        //then
        assertTrue(borrowedBook.isPresent());
        assertEquals(bookId, borrowedBook.get().getIdAsLong());
    }
}
