package customers;

import database.Database;
import products.Order;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CustomerRepository {
    Database database;

    public CustomerRepository(Database database) {
        this.database = database;
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
        String sql = "INSERT INTO Orders (id_customer,order_data,price) VALUES (?,?,?)";
        try (PreparedStatement preparedStatement = database.getConnection().prepareStatement(sql)) {
            preparedStatement.setInt(1, order.getIdCustomer());
            preparedStatement.setDate(2, Date.valueOf(order.getOrderData()));
            preparedStatement.setDouble(3, order.getPrice());
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
                " c.email = ? AND cp.password = ?";
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
    public void modifyCustomersColumn(String name,String latName,String email,int number,String address,String password) {
        String sql = "UPDATE Customers SET name = ?,lastName = ?, email = ?, number = ?, address = ?, password = ?";
        try(PreparedStatement preparedStatement = database.getConnection().prepareStatement(sql)) {
            preparedStatement.setString(1, name);
            preparedStatement.setString(2,latName);
            preparedStatement.setString(3,email);
            preparedStatement.setInt(4,number);
            preparedStatement.setString(5,address);
            preparedStatement.setString(6,password);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public int getCustomerIdByEmail(String email) {
        String sql = "SELECT id_customer FROM Customers WHERE email = ?";
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


}
