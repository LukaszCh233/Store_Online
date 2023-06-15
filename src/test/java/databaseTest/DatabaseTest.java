package databaseTest;

import database.Database;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.SQLException;

public class DatabaseTest {
    Database database;
    Connection connection;

    @BeforeEach
    void setUp() throws SQLException {
        database = new Database("dataBase");
        connection = database.getConnection();
    }

    @AfterEach
    void closeDatabase() throws Exception {
        database.close();
    }

    @Test
    void connectionTest() {
        Assertions.assertNotNull(connection);
    }

    @Test
    void initializeDatabaseTest() throws SQLException {
        Assertions.assertNotNull(connection.createStatement().executeQuery("SELECT * FROM Categories"));
        Assertions.assertNotNull(connection.createStatement().executeQuery("SELECT * FROM Products"));
        Assertions.assertNotNull(connection.createStatement().executeQuery("SELECT * FROM Customers"));
        Assertions.assertNotNull(connection.createStatement().executeQuery("SELECT * FROM Orders"));
        Assertions.assertNotNull(connection.createStatement().executeQuery("SELECT * FROM Customers_password"));
    }
}
