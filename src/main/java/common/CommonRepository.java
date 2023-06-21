package common;

import customers.Customer;
import database.Database;
import products.Category;
import products.Product;
import products.Status;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collection;

public class CommonRepository {
    Database database;

    public CommonRepository(Database database) {
        this.database = database;
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
        return new Product(idProduct, name, price, quantity, id_category, status);
    }

    public static Customer mapResultSetToCustomer(ResultSet resultSet) throws SQLException {
        Integer idCustomer = resultSet.getInt(1);
        String name = resultSet.getString(2);
        String lastName = resultSet.getString(3);
        String email = resultSet.getString(4);
        int number = resultSet.getInt(5);
        String address = resultSet.getString(6);
        String password = resultSet.getString(8);

        return new Customer(idCustomer, name, lastName, email, number, address, password);
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

    public Collection<Product> loadProductFromDatabase() {
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

    public Collection<Product> loadSelectedCategoryProduct(int categoryId) {
        Collection<Product> products = new ArrayList<>();
        String sql = "SELECT * FROM Products WHERE id_category = ?";
        try (PreparedStatement statement = database.getConnection().prepareStatement(sql)) {
            statement.setInt(1, categoryId);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                products.add(mapResultSetToProduct(resultSet));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return products;
    }
    public boolean categoryExists(int id_category) {
        String sql = "SELECT id_category FROM categories WHERE id_category = ? LIMIT 1";
        try (PreparedStatement preparedStatement = database.getConnection().prepareStatement(sql)) {
            preparedStatement.setInt(1,id_category);
            ResultSet resultSet = preparedStatement.executeQuery();
            return resultSet.next();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    public boolean productExists(int idProduct) {
        String sql = "SELECT id_product FROM Products WHERE id_product = ? LIMIT 1 ";
        try (PreparedStatement preparedStatement = database.getConnection().prepareStatement(sql)) {
            preparedStatement.setInt(1,idProduct);
            ResultSet resultSet = preparedStatement.executeQuery();
            return  resultSet.next();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}
