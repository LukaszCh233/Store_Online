package customersTest;

import administrator.AdministratorRepository;
import common.CommonRepository;
import customers.Basket;
import customers.Customer;
import customers.CustomerFunctions;
import customers.CustomerRepository;
import database.Database;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import products.Order;
import products.Product;
import products.Status;
import java.io.File;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;

public class CustomerRepositoryTest {
    private static final String DATABASE_NAME = "test";
    Database database;
    AdministratorRepository administratorRepository;
    CommonRepository commonRepository;
    CustomerRepository customerRepository;
    CustomerFunctions customerFunctions;

    @BeforeEach
    void createDatabase() throws SQLException {
        database = new Database(DATABASE_NAME);
        administratorRepository = new AdministratorRepository(database);
        commonRepository = new CommonRepository(database);
        customerRepository = new CustomerRepository(database);
        customerFunctions = new CustomerFunctions(database);

    }

    @AfterEach
    void deleteDatabase() throws Exception {
        database.close();
        File file = new File(DATABASE_NAME);
        boolean deleted = file.delete();
        if (!deleted) {
            throw new RuntimeException("Failed to delete database file: " + DATABASE_NAME);
        }
    }

    @Test
    void shouldSaveCustomerToDatabaseTest() {

        //given
        Customer customer = new Customer(1, "testName", "testLastName", "testEmail", 12345, "testAddress", "testPassword");

        //when
        customerRepository.saveCustomerToDatabase(customer);

        //then
        System.out.println(customer);
        Collection<Customer> customers = commonRepository.loadCustomersFromDatabase();
        System.out.println(customers);
        Assertions.assertEquals(1, customers.size());
        Assertions.assertTrue(customers.contains(customer));
    }

    @Test
    void shouldSaveOrderToDatabaseTest() {

        //given
        LocalDate data = LocalDate.now();
        Order order = new Order(1, 1, data, 100.0, Status.ORDERED);

        //when
        customerRepository.saveOrderToDatabase(order);

        //then
        Collection<Order> orders = administratorRepository.loadOrders();
        Assertions.assertEquals(1, orders.size());
        Assertions.assertTrue(orders.contains(order));
    }

    @Test
    void shouldUpdateProductQuantityInDatabase() {

        //given
        Product product = new Product(1, "testName", 100.0, 10, 1, Status.AVAILABLE);
        administratorRepository.saveProduct(product);

        //when
        customerRepository.updateProductQuantityInDatabase(product.getId_product(), 20);

        //then
        Collection<Product> products = commonRepository.loadProduct();
        Product updateProductQuantity = new ArrayList<>(products).get(0);
        Assertions.assertEquals(20, updateProductQuantity.getQuantity());
    }

    @Test
    void shouldLoginTest() {

        //given
        Customer customer = new Customer(1, "testName", "testLastName", "testEmail", 12345, "testAddress", "testPassword");
        customerRepository.saveCustomerToDatabase(customer);

        //when
        boolean loginResult = customerRepository.login(customer.getEmail(), customer.getPassword());

        //then
        Assertions.assertTrue(loginResult, "Login should be successful");
    }

    @Test
    void shouldModifyCustomersColumnInDatabaseTest() {

        //given
        Customer customer = new Customer(1, "testName", "testLastName", "testEmail", 12345, "testAddress", "testPassword");
        customerRepository.saveCustomerToDatabase(customer);

        //when
        customerRepository.modifyCustomersColumnInDatabase(1, "newName", "newLastName", 54321, "newAddress");

        //then
        Collection<Customer> customers = commonRepository.loadCustomersFromDatabase();
        Customer modifyCustomer = new ArrayList<>(customers).get(0);
        Assertions.assertEquals("newName", modifyCustomer.getName());
        Assertions.assertEquals("newLastName", modifyCustomer.getLastName());
        Assertions.assertEquals(54321, modifyCustomer.getNumber());
        Assertions.assertEquals("newAddress", modifyCustomer.getAddress());
    }

    @Test
    void shouldModifyCustomersPasswordColumnInDatabaseTest() {

        //given
        Customer customer = new Customer(1, "testName", "testLastName", "testEmail", 12345, "testAddress", "testPassword");
        customerRepository.saveCustomerToDatabase(customer);

        //when
        customerRepository.modifyCustomersPasswordColumnInDatabase(1, "newEmail", "newPassword");

        //then
        Customer modifiedCustomer = customerFunctions.getCustomerById(1);
        Assertions.assertEquals("newEmail", modifiedCustomer.getEmail());
        Assertions.assertEquals("newPassword", modifiedCustomer.getPassword());
    }
    @Test
    void shouldGetCustomerIdByEmailTest() {

        // Given
        Customer customer = new Customer(1, "testName", "testLastName", "testEmail", 12345, "testAddress", "testPassword");
        customerRepository.saveCustomerToDatabase(customer);

        // When
        int customerId = customerRepository.getCustomerIdByEmail(customer.getEmail());

        // Then
        Assertions.assertEquals(1, customerId);
    }
    @Test
    void shouldLoadOrdersFromDatabaseTest() {
        //given
        LocalDate data = LocalDate.now();
        Order order = new Order(1, 1, data, 100.0, Status.ORDERED);
        Order order1 = new Order(2, 1, data, 1000.0, Status.ORDERED);
        customerRepository.saveOrderToDatabase(order);
        customerRepository.saveOrderToDatabase(order1);

        //when
        customerRepository.loadOrders(order1.getIdCustomer());

        //then
        Collection<Order> loadedOrders = customerRepository.loadOrders(1);
        Assertions.assertEquals(2, loadedOrders.size());
        Assertions.assertTrue(loadedOrders.contains(order));
        Assertions.assertTrue(loadedOrders.contains(order1));
    }
    @Test
    void shouldSaveProductToBasketDatabaseTest() {

        //given
        Product product = new Product(1, "testName", 100.0, 10, 1, Status.AVAILABLE);
        administratorRepository.saveProduct(product);
        Basket basketProduct = new Basket(1, product.getId_product(), product.getName(), product.getPrice(), product.getQuantity());

        //when
        customerRepository.saveProductToBasketDatabase(basketProduct);

        //then
        Collection<Basket> basket = customerRepository.loadBasketFromDatabase();
        Assertions.assertEquals(1, basket.size());
        Assertions.assertTrue(basket.contains(basketProduct));
    }
    @Test
    void shouldLoadBasketFromDatabaseTest() {

        //given
        Basket productBasket = new Basket(1, 1, "test", 100.0, 100);
        Basket productBasket1 = new Basket(2, 2, "test1", 1000.0, 100);
        customerRepository.saveProductToBasketDatabase(productBasket);
        customerRepository.saveProductToBasketDatabase(productBasket1);

        //when
        Collection<Basket> basket = customerRepository.loadBasketFromDatabase();

        //then
        Assertions.assertEquals(2, basket.size());
        Assertions.assertTrue(basket.contains(productBasket));
        Assertions.assertTrue(basket.contains(productBasket1));
    }
    @Test
    void shouldRemoveProductFromBasketDatabaseTest() {

        //given
        Basket productBasket = new Basket(null, 1, "test", 100.0, 100);
        Basket productBasket1 = new Basket(null, 2, "test1", 1000.0, 100);
        customerRepository.saveProductToBasketDatabase(productBasket);
        customerRepository.saveProductToBasketDatabase(productBasket1);

        //when
        customerRepository.removeProductFromBasketDatabase(1);
        customerRepository.removeProductFromBasketDatabase(2);

        //then
        Collection<Basket> basket = customerRepository.loadBasketFromDatabase();
        Assertions.assertTrue(basket.isEmpty());
    }
    @Test
    void shouldUpdateQuantityInBasketTest() {

        //given
        Basket productBasket = new Basket(null, 1, "test", 100.0, 100);
        customerRepository.saveProductToBasketDatabase(productBasket);

        //when

        customerRepository.updateQuantityInBasket(1, 50);

        //then
        Collection<Basket> basket = customerRepository.loadBasketFromDatabase();
        Basket updateQuantity = basket.iterator().next();
        Assertions.assertEquals(50, updateQuantity.getQuantity());
    }
    @Test
    void shouldClearBasketDatabaseTest() {

        //given
        Basket productBasket = new Basket(null, 1, "test", 100.0, 100);
        Basket productBasket1 = new Basket(null, 2, "test1", 1000.0, 100);
        customerRepository.saveProductToBasketDatabase(productBasket);
        customerRepository.saveProductToBasketDatabase(productBasket1);

        //when
        customerRepository.clearBasketDatabase();

        //then
        Collection<Basket> basket = customerRepository.loadBasketFromDatabase();
        Assertions.assertTrue(basket.isEmpty());
    }
}
