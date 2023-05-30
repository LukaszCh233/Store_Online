package customers;

import database.Database;
import products.Order;
import products.Status;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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
        String query = "INSERT INTO Customers_password (password, email) VALUES ( ?, ?)";
        try (PreparedStatement statement = database.getConnection().prepareStatement(query)) {
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
            preparedStatement.setInt(5,idCustomer);

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public void modifyCustomersPasswordColumnInDatabase(int idCustomer,String email,String password) {
        String sql = "UPDATE Customers_password SET email = ?, password = ? WHERE customer_id = ?";
        try (PreparedStatement preparedStatement = database.getConnection().prepareStatement(sql)) {
            preparedStatement.setString(1, email);
            preparedStatement.setString(2,password);
            preparedStatement.setInt(3,idCustomer);

            preparedStatement.executeUpdate();
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

    public Collection<Order> loadOrders(int id) {
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
}
