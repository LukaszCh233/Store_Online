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
import java.io.ByteArrayInputStream;
import java.io.File;
import java.sql.SQLException;
import java.util.Collection;

public class CustomerFunctionsTest {
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
    void shouldRegisterCustomerTest() {

        //given
        String input = "testName\ntestLastName\ntestEmail\n12345\ntestAddress\ntestPassword\nyes\n";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(input.getBytes());
        System.setIn(inputStream);

        //when
        customerFunctions.registerCustomer();

        //then
        Collection<Customer> customers = commonRepository.loadCustomersFromDatabase();
        Assertions.assertEquals(1, customers.size());
    }

    @Test
    void shouldLoginCustomerTest() {

        //given
        Customer customer = new Customer(1, "testName", "testLastName", "testEmail", 12345, "testAddress", "testPassword");
        customerRepository.saveCustomerToDatabase(customer);

        //when
        String email = "testEmail";
        String password = "testPassword";
        String input = email + "\n" + password + "\n";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(input.getBytes());
        System.setIn(inputStream);
        customerFunctions.loginCustomer();

        //then
        int actualCustomerId = customerRepository.getCustomerIdByEmail(email);
        Assertions.assertEquals(1, actualCustomerId);
        Assertions.assertTrue(customerRepository.login(email, password));
    }

    @Test
    void shouldAddProductToBasketTest() {

        //given
        Product product = new Product(1, "testName", 100.0, 10, 1, Status.AVAILABLE);
        administratorRepository.saveProduct(product);

        //when
        String input = "1\n10\nyes\n";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(input.getBytes());
        System.setIn(inputStream);
        customerFunctions.addProductToBasket();

        //then
        Collection<Basket> basket = customerRepository.loadBasketFromDatabase();
        Assertions.assertEquals(1, basket.size());
    }

    @Test
    void shouldDisplayProductInBasketTest() {

        //given
        Basket productBasket = new Basket(1, 1, "test", 100.0, 100);
        Basket productBasket1 = new Basket(2, 2, "test1", 200.0, 100);
        customerRepository.saveProductToBasketDatabase(productBasket);
        customerRepository.saveProductToBasketDatabase(productBasket1);

        //when
        customerFunctions.displayProductsInBasket();

        //then
        Collection<Basket> basket = customerRepository.loadBasketFromDatabase();
        Assertions.assertEquals(2, basket.size());
        Assertions.assertTrue(basket.contains(productBasket));
        Assertions.assertTrue(basket.contains(productBasket1));
    }

    @Test
    void shouldRemoveProductFromBasket() {

        //given
        Basket basketProduct = new Basket(1, 1, "product", 100.0, 10);
        customerRepository.saveProductToBasketDatabase(basketProduct);

        //when
        String input = "yes\n1\n10\nno\n";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(input.getBytes());
        System.setIn(inputStream);
        customerFunctions.removeProductFromBasket();

        //then
        Collection<Basket> basket = customerRepository.loadBasketFromDatabase();
        Assertions.assertTrue(basket.isEmpty());
    }

    @Test
    void shouldSubmitOrderTest() {

        //given
        Customer customer = new Customer(1, "name", "lastName", "email", 123, "address", "password");
        Customer customer1 = new Customer(2, "name", "lastName", "email1", 123, "address", "password");
        customerRepository.saveCustomerToDatabase(customer);
        customerRepository.saveCustomerToDatabase(customer1);
        Basket basketProduct = new Basket(null, 1, "product", 100.0, 10);
        customerRepository.saveProductToBasketDatabase(basketProduct);

        //when
        String input = "email1";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(input.getBytes());
        System.setIn(inputStream);
        customerFunctions.submitOrder();

        //then
        Collection<Order> orders = administratorRepository.loadOrders();
        Assertions.assertEquals(1, orders.size());
        Order order = orders.iterator().next();
        Assertions.assertEquals(1, order.getIdOrder());
        Assertions.assertEquals(customer1.getId_customer(), order.getIdCustomer());
        Assertions.assertEquals(Status.ORDERED, order.getStatus());
    }

    @Test
    void shouldChangeAccountDataTest() {

        //given
        String email = "email";
        Customer customer = new Customer(1, "name", "lastName", email, 123, "address", "password");
        customerRepository.saveCustomerToDatabase(customer);

        //when
        String input = "yes\n2\nnewName\nnewLastName\n321\nnewAddress\nyes\n";
        String input1 = "yes\n1\nnewEmail\nnewPassword\nyes\n";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(input.getBytes());
        System.setIn(inputStream);
        customerFunctions.changeAccountData(email);
        ByteArrayInputStream inputStream1 = new ByteArrayInputStream(input1.getBytes());
        System.setIn(inputStream1);
        customerFunctions.changeAccountData(email);

        //then
        Customer loggedCustomer = customerFunctions.getCustomerById(customer.getId_customer());
        Assertions.assertEquals("newName", loggedCustomer.getName());
        Assertions.assertEquals("newLastName", loggedCustomer.getLastName());
        Assertions.assertEquals(321, loggedCustomer.getNumber());
        Assertions.assertEquals("newAddress", loggedCustomer.getAddress());
        Assertions.assertEquals("newEmail", loggedCustomer.getEmail());
        Assertions.assertEquals("newPassword", loggedCustomer.getPassword());
    }
}


