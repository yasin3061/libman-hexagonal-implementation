package com.yasinbee.libman.hex.domain.borrowing.infrastructure.mapper;

import com.yasinbee.libman.hex.domain.borrowing.core.model.BorrowedBook;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;


public class BorrowedBookRowMapper implements RowMapper<BorrowedBook> {

    @Override
    public BorrowedBook mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new BorrowedBook(
                rs.getLong("book_id"),
                rs.getLong("user_id"),
                rs.getTimestamp("borrowed_date").toInstant()
        );
    }
}
