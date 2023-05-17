package administrator;

import customers.Customer;
import database.Database;
import products.Category;
import products.Order;
import products.Product;
import products.Status;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collection;

public class AdministratorRepository {
    Database database;

    public AdministratorRepository(Database database) {
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


        return new Product(idProduct, name, price, quantity, id_category,status);
    }

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

    public static Order mapResultSetToOrder(ResultSet resultSet) throws SQLException {
        Integer idOrder = resultSet.getInt(1);
        Integer idCustomer = resultSet.getInt(2);
        Date orderData = resultSet.getDate(3);
        Double price = resultSet.getDouble(4);
        return new Order(idOrder, idCustomer, orderData.toLocalDate(), price);
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

    public void addCategoryToDatabase(Category category) {
        String sql = "INSERT INTO Categories (name) VALUES (?)";
        try (PreparedStatement preparedStatement = database.getConnection().prepareStatement(sql)) {
            preparedStatement.setString(1, category.getCategory());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
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

    public void saveProduct(Product product) {
        String sql = "INSERT INTO Products (name,price,quantity,id_category) VALUES (?,?,?,?)";
        try (PreparedStatement preparedStatement = database.getConnection().prepareStatement(sql)) {
            preparedStatement.setString(1, product.getName());
            preparedStatement.setDouble(2, product.getPrice());
            preparedStatement.setInt(3, product.getQuantity());
            preparedStatement.setInt(4, product.getId_category());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
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

    public Collection<Order> loadOrders() {
        Collection<Order> orders = new ArrayList<>();
        String sql = " SELECT * FROM Orders";
        try (Statement statement = database.getConnection().createStatement()) {
            ResultSet resultSet = statement.executeQuery(sql);
            while (resultSet.next()) {
                orders.add(mapResultSetToOrder(resultSet));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return orders;
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

    public void removeProductFromDatabase(int id) {
        String sql = "DELETE FROM Products WHERE id_product = ?";
        try (PreparedStatement preparedStatement = database.getConnection().prepareStatement(sql)) {
            preparedStatement.setInt(1, id);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void removeCategoryFromDatabase(int id) {
        String sql = " DELETE FROM Categories WHERE id_category = ?";
        try (PreparedStatement preparedStatement = database.getConnection().prepareStatement(sql)) {
            preparedStatement.setInt(1, id);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void modifyProductsColumn(int id, String name, double price, int quantity, int id_category) {
        String sql = "UPDATE Products SET name = ?, price = ?, quantity = ?, id_category = ? WHERE id_product = ?";
        try (PreparedStatement preparedStatement = database.getConnection().prepareStatement(sql)) {
            preparedStatement.setString(1, name);
            preparedStatement.setDouble(2, price);
            preparedStatement.setInt(3, quantity);
            preparedStatement.setInt(4, id_category);
            preparedStatement.setInt(5, id);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}



