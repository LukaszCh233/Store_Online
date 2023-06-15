package customersTest;

import customers.Customer;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import java.util.Objects;


public class CustomersTest {
    @Test
     void shouldCreateCustomerObject() {
        // given
        Integer customerId = 1;
        String name = "John";
        String lastName = "Doe";
        String email = "john.doe@example.com";
        Integer number = 12345;
        String address = "123 Main St";
        String password = "password123";

        // when
        Customer customer = new Customer(customerId, name, lastName, email, number, address, password);

        // then
        Assertions.assertEquals(customerId, customer.getId_customer());
        Assertions.assertEquals(name, customer.getName());
        Assertions.assertEquals(lastName, customer.getLastName());
        Assertions.assertEquals(email, customer.getEmail());
        Assertions.assertEquals(number, customer.getNumber());
        Assertions.assertEquals(address, customer.getAddress());
        Assertions.assertEquals(password, customer.getPassword());
    }
    @Test
    void shouldCheckEqualityOfCustomers() {
        // given
        Customer customer1 = new Customer(1, "John", "Doe", "john.doe@example.com", 12345, "123 Main St", "password123");
        Customer customer2 = new Customer(1, "John", "Doe", "john.doe@example.com", 12345, "123 Main St", "password123");

        // then
        Assertions.assertEquals(customer1, customer2);
    }
    @Test
    void shouldGenerateCorrectHashCode() {
        // given
        Customer customer = new Customer(1, "John", "Doe", "john.doe@example.com", 12345, "123 Main St", "password123");
        int expectedHashCode = Objects.hash(1, "John", "Doe", "john.doe@example.com", 12345, "123 Main St", "password123");

        // then
        Assertions.assertEquals(expectedHashCode, customer.hashCode());
    }
}
