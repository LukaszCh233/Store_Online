package common;

import customers.Customer;
import database.Database;
import products.Category;
import products.Product;
import products.Status;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;

public class CommonRepository {
    Database database;

    public CommonRepository(Database database) {
        this.database = database;
    }

    public Collection<Category> loadCategoriesFromDatabase() {
        Collection<Category> categories = new ArrayList<>();
        String sql = "SELECT * FROM Categories";
        try (Statement statement = database.getConnection().createStatement()) {
            ResultSet resultSet = statement.executeQuery(sql);
            while (resultSet.next()) {
                categories.add(mapResultSetToCategory(resultSet));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return categories;
    }
    public Collection<Customer> loadCustomersFromDatabase() {
        Collection<Customer> customers = new ArrayList<>();
        String sql = "SELECT * FROM Customers c JOIN Customers_password cp ON c.id_customer = cp.customer_id";
        try (Statement statement = database.getConnection().createStatement()) {
            ResultSet resultSet = statement.executeQuery(sql);
            while (resultSet.next()) {
                customers.add(mapResultSetToCustomer(resultSet));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return customers;
    }
    public Collection<Product> loadProduct() {
        Collection<Product> products = new ArrayList<>();
        String sql = "SELECT * FROM Products";
        try (Statement statement = database.getConnection().createStatement()) {
            ResultSet resultSet = statement.executeQuery(sql);
            while (resultSet.next()) {
                products.add(mapResultSetToProduct(resultSet));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return products;
    }

    public static Category mapResultSetToCategory(ResultSet resultSet) throws SQLException {
        int id_category = resultSet.getInt(1);
        String name = resultSet.getString(2);
        return new Category(id_category, name);
    }
    public static Product mapResultSetToProduct(ResultSet resultSet) throws SQLException {
        Integer idProduct = resultSet.getInt(1);
        String name = resultSet.getString(2);
        Double price = resultSet.getDouble(3);
        int quantity = resultSet.getInt(4);
        Integer id_category = resultSet.getInt(5);
        String statusString = resultSet.getString(6);
        Status status;
        if (statusString != null) {
            status = Status.valueOf(resultSet.getString(6));
        } else {
            status = Status.AVAILABLE;

        }
        return new Product(idProduct, name, price, quantity, id_category,status);    }
    public static Customer mapResultSetToCustomer(ResultSet resultSet) throws SQLException {
        Integer idCustomer = resultSet.getInt(1);
        String name = resultSet.getString(2);
        String lastName = resultSet.getString(3);
        String email = resultSet.getString(4);
        int number = resultSet.getInt(5);
        String address = resultSet.getString(6);
        String password = resultSet.getString(7);

        return new Customer(idCustomer, name, lastName, email, number, address,password);

    }
}
