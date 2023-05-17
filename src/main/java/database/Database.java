package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;

public class Database implements AutoCloseable {
    private static final String JDBC_SQLITE_SHOP_DB = "jdbc:sqlite:";
    private final Connection connection;

    public Database(String path) throws SQLException {
        connection = DriverManager.getConnection(JDBC_SQLITE_SHOP_DB + path);
        initializeDatabase();
    }

    public Database() throws SQLException {
        this("Shop.db");
    }

    private void initializeDatabase() {
        Collection<String> sqlTable = new ArrayList<>();
        sqlTable.add("CREATE TABLE IF NOT EXISTS Categories (" +
                "id_category INTEGER PRIMARY KEY AUTOINCREMENT," +
                "name TEXT" +
                ");");
        sqlTable.add("CREATE TABLE IF NOT EXISTS Products (" +
                "id_product INTEGER PRIMARY KEY AUTOINCREMENT," +
                "name TEXT ," +
                "price REAL," +
                "quantity INTEGER," +
                "id_category INTEGER," +
                "FOREIGN KEY (id_category) REFERENCES Categories (id_category)" +
                ");");
        sqlTable.add("CREATE TABLE IF NOT EXISTS Customers (" +
                "id_customer INTEGER PRIMARY KEY AUTOINCREMENT," +
                "name TEXT," +
                "lastName TEXT," +
                "email TEXT," +
                "number INTEGER," +
                "address TEXT" +
                ");");
        sqlTable.add("CREATE TABLE IF NOT EXISTS Orders (" +
                "id_order INTEGER PRIMARY KEY AUTOINCREMENT," +
                "id_customer INTEGER," +
                "order_data DATE," +
                "price INTEGER," +
                "status TEXT" +
                ");");
        sqlTable.add("CREATE TABLE IF NOT EXISTS Customers_password (" +
                "customer_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "password TEXT," +
                "email TEXT" +
                ");");

        for (String sql : sqlTable) {
            try (PreparedStatement preparedStatement = getConnection().prepareStatement(sql)) {
                preparedStatement.execute();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public Connection getConnection() {
        return connection;
    }

    @Override
    public void close() throws Exception {
        System.out.println("Database connection closed.");
        connection.close();
    }
}

