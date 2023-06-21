package administrator;

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

    public static Order mapResultSetToOrder(ResultSet resultSet) throws SQLException {
        Integer idOrder = resultSet.getInt(1);
        Integer idCustomer = resultSet.getInt(2);
        Date orderData = resultSet.getDate(3);
        Double price = resultSet.getDouble(4);
        String statusString = resultSet.getString(5);
        Status status;
        if (!statusString.equals(Status.ORDERED.name())) {
            if (statusString.equals(Status.SENT.name())) {
                status = Status.SENT;
            } else {
                status = Status.valueOf(statusString);
            }
        } else {
            status = Status.ORDERED;
        }
        return new Order(idOrder, idCustomer, orderData.toLocalDate(), price, status);
    }

    public void saveCategoryToDatabase(Category category) {
        String sql = "INSERT INTO Categories (name) VALUES (?)";
        try (PreparedStatement preparedStatement = database.getConnection().prepareStatement(sql)) {
            preparedStatement.setString(1, category.getCategory());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
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

    public Collection<Order> loadOrdersFromDatabase() {
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

    public void modifyProductsColumnInDatabase(int id, String name, double price, int quantity, int id_category) {
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

    public void updateOrderStatusInDatabase(int idOrder, Status status) {
        String sql = "UPDATE Orders SET status = ? WHERE id_order = ?";
        try (PreparedStatement preparedStatement = database.getConnection().prepareStatement(sql)) {
            preparedStatement.setString(1, status.name());
            preparedStatement.setInt(2, idOrder);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}




