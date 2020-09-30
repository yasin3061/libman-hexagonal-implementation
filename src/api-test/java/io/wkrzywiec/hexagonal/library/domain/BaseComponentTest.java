package io.wkrzywiec.hexagonal.library.domain;

import com.yasinbee.libman.hex.DatabaseHelper;
import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.jdbc.core.JdbcTemplate;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public abstract class BaseComponentTest {

    protected String baseURL;
    @Autowired
    protected JdbcTemplate jdbcTemplate;
    protected DatabaseHelper databaseHelper;
    @LocalServerPort
    private int port;

    @BeforeEach
    public void init() {
        this.baseURL = "http://localhost:" + port;
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
        databaseHelper = new DatabaseHelper(jdbcTemplate);
    }
}
