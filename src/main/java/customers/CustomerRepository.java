package customers;

import database.Database;
import products.Order;
import products.Status;
import java.sql.*;
import java.util.ArrayList;
import java.util.Collection;

public class CustomerRepository {
    Database database;

    public CustomerRepository(Database database) {
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

    public static Basket mapResultSetToBasket(ResultSet resultSet) throws SQLException {
        Integer idBasket = resultSet.getInt(1);
        Integer idProduct = resultSet.getInt(2);
        String name = resultSet.getString(3);
        double price = resultSet.getDouble(4);
        int quantity = resultSet.getInt(5);

        return new Basket(idBasket, idProduct, name, price, quantity);
    }

    public void updateProductQuantityInDatabase(int productId, int quantity) {
        String sql = "UPDATE Products SET quantity = ? WHERE id_product = ?";
        try (PreparedStatement preparedStatement = database.getConnection().prepareStatement(sql)) {
            preparedStatement.setInt(1, quantity);
            preparedStatement.setInt(2, productId);
            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Product quantity updated");
            } else {
                System.out.println("Updated failed");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void saveOrderToDatabase(Order order) {
        String sql = "INSERT INTO Orders (id_customer,order_data,price,status) VALUES (?,?,?,?)";
        try (PreparedStatement preparedStatement = database.getConnection().prepareStatement(sql)) {
            preparedStatement.setInt(1, order.getIdCustomer());
            preparedStatement.setDate(2, Date.valueOf(order.getOrderData()));
            preparedStatement.setDouble(3, order.getPrice());
            preparedStatement.setString(4, String.valueOf(order.getStatus()));
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void saveCustomerToDatabase(Customer customer) {
        String sql = "INSERT INTO Customers (name,lastName,email,number,address) VALUES (?,?,?,?,?)";
        try (PreparedStatement preparedStatement = database.getConnection().prepareStatement(sql)) {
            preparedStatement.setString(1, customer.name);
            preparedStatement.setString(2, customer.lastName);
            preparedStatement.setString(3, customer.email);
            preparedStatement.setInt(4, customer.number);
            preparedStatement.setString(5, customer.address);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        String sql1 = "INSERT INTO Customers_password (password, email) VALUES (?, ?)";
        try (PreparedStatement statement = database.getConnection().prepareStatement(sql1)) {
            statement.setString(1, customer.password);
            statement.setString(2, customer.email);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean login(String email, String password) {
        String sql = "SELECT COUNT(*) FROM Customers c JOIN Customers_password cp ON c.id_customer = cp.customer_id WHERE" +
                " cp.email = ? AND cp.password = ?";
        try (PreparedStatement preparedStatement = database.getConnection().prepareStatement(sql)) {
            preparedStatement.setString(1, email);
            preparedStatement.setString(2, password);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                int count = resultSet.getInt(1);
                return count > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public void modifyCustomersColumnInDatabase(int idCustomer, String name, String lastName, int number, String address) {
        String sql = "UPDATE Customers " +
                "SET name = ?, lastName = ?, number = ?, address = ? " +
                "WHERE id_customer = ? ";

        try (PreparedStatement preparedStatement = database.getConnection().prepareStatement(sql)) {
            preparedStatement.setString(1, name);
            preparedStatement.setString(2, lastName);
            preparedStatement.setInt(3, number);
            preparedStatement.setString(4, address);
            preparedStatement.setInt(5, idCustomer);

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void modifyCustomersPasswordColumnInDatabase(int idCustomer, String email, String password) {
        String sql = "UPDATE Customers_password SET email = ?, password = ? WHERE customer_id = ?";
        try (PreparedStatement preparedStatement = database.getConnection().prepareStatement(sql)) {
            preparedStatement.setString(1, email);
            preparedStatement.setString(2, password);
            preparedStatement.setInt(3, idCustomer);

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        String passwordSql = "UPDATE Customers SET email = ? WHERE id_customer = ?";
        try (PreparedStatement passwordStatement = database.getConnection().prepareStatement(passwordSql)) {
            passwordStatement.setString(1, email);
            passwordStatement.setInt(2, idCustomer);
            passwordStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public int getCustomerIdByEmail(String email) {
        String sql = "SELECT customer_id FROM Customers_password WHERE email = ?";
        try (PreparedStatement preparedStatement = database.getConnection().prepareStatement(sql)) {
            preparedStatement.setString(1, email);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    public Collection<Order> loadOrderFromDatabase(int id) {
        Collection<Order> orders = new ArrayList<>();
        String sql = " SELECT * FROM Orders WHERE id_customer = ?";
        try (PreparedStatement statement = database.getConnection().prepareStatement(sql)) {
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                orders.add(mapResultSetToOrder(resultSet));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return orders;
    }

    public void saveProductToBasketDatabase(Basket basketProduct) {
        String sql = "INSERT INTO Basket (id_product,name,price,quantity) VALUES (?,?,?,?)";
        try (PreparedStatement preparedStatement = database.getConnection().prepareStatement(sql)) {
            preparedStatement.setInt(1, basketProduct.getId_product());
            preparedStatement.setString(2, basketProduct.getName());
            preparedStatement.setDouble(3, basketProduct.getPrice());
            preparedStatement.setInt(4, basketProduct.getQuantity());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Collection<Basket> loadBasketFromDatabase() {
        Collection<Basket> basket = new ArrayList<>();
        String sql = "SELECT * FROM Basket";
        try (Statement statement = database.getConnection().createStatement()) {
            ResultSet resultSet = statement.executeQuery(sql);
            while (resultSet.next()) {
                basket.add(mapResultSetToBasket(resultSet));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return basket;
    }

    public void removeProductFromBasketDatabase(int id) {
        String sql = "DELETE FROM Basket WHERE id_product = ?";
        try (PreparedStatement preparedStatement = database.getConnection().prepareStatement(sql)) {
            preparedStatement.setInt(1, id);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateQuantityInBasket(int id, int quantity) {
        String sql = "UPDATE Basket SET  quantity = ? WHERE id_product = ?";
        try (PreparedStatement preparedStatement = database.getConnection().prepareStatement(sql)) {
            preparedStatement.setInt(1, quantity);
            preparedStatement.setInt(2, id);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void clearBasketDatabase() {
        String sql = "DELETE FROM Basket WHERE 1=1";
        try (PreparedStatement preparedStatement = database.getConnection().prepareStatement(sql)) {
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
