package administratorTest;

import administrator.AdministratorFunctions;
import administrator.AdministratorRepository;
import common.CommonRepository;
import customers.Customer;
import customers.CustomerRepository;
import database.Database;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import products.Category;
import products.Order;
import products.Product;
import products.Status;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;

public class AdministratorFunctionTest {
    private static final String DATABASE_NAME = "test";
    Database database;
    AdministratorRepository administratorRepository;
    AdministratorFunctions administratorFunctions;
    CommonRepository commonRepository;
    CustomerRepository customerRepository;

    @BeforeEach
    void createDatabase() throws SQLException {
        database = new Database(DATABASE_NAME);
        administratorRepository = new AdministratorRepository(database);
        commonRepository = new CommonRepository(database);
        customerRepository = new CustomerRepository(database);
        administratorFunctions = new AdministratorFunctions(database);

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
    void shouldCreateCategoryTest() {

        //given
        String input = "yes\ncategoryName\nno\n";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(input.getBytes());
        System.setIn(inputStream);

        //when
        administratorFunctions.createCategory();

        //then
        Collection<Category> categories = commonRepository.loadCategoriesFromDatabase();
        Assertions.assertEquals(1, categories.size());
    }

    @Test
    void shouldDeleteCategoryTest() {

        //given
        Category category = new Category(1, "sport");
        administratorRepository.saveCategoryToDatabase(category);

        //when
        String input = "yes\n1\nno";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(input.getBytes());
        System.setIn(inputStream);
        administratorFunctions.deleteCategory();

        //then
        Collection<Category> categories = commonRepository.loadCategoriesFromDatabase();
        Assertions.assertTrue(categories.isEmpty());
    }

    @Test
    void shouldAddProductToStoreTest() {

        //given
        Category category = new Category(1, "sport");
        administratorRepository.saveCategoryToDatabase(category);

        //when
        String input = "testName\n100\n10\n1\nyes\n";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(input.getBytes());
        System.setIn(inputStream);
        administratorFunctions.addProductToStore();

        //then
        Collection<Product> products = commonRepository.loadProductFromDatabase();
        Product foundProduct = new ArrayList<>(products).get(0);
        Assertions.assertEquals(1, products.size());
        Assertions.assertTrue(products.contains(foundProduct));
        Assertions.assertEquals("testName", foundProduct.getName());
        Assertions.assertEquals(100, foundProduct.getPrice());
        Assertions.assertEquals(10, foundProduct.getQuantity());
        Assertions.assertEquals(1, foundProduct.getId_category());
    }

    @Test
    void shouldDeleteProductFromStoreTest() {

        //given
        Product product = new Product(1, "testName", 100.0, 10, 1, Status.AVAILABLE);
        administratorRepository.saveProduct(product);

        //when
        String input = "yes\n1\nno\n";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(input.getBytes());
        System.setIn(inputStream);
        administratorFunctions.deleteProductFromStore();

        //then
        Collection<Product> products = commonRepository.loadProductFromDatabase();
        Assertions.assertTrue(products.isEmpty());
    }

    @Test
    void shouldModifyProductDataTest() {

        //given
        Category category = new Category(1, "sport");
        administratorRepository.saveCategoryToDatabase(category);
        Product product = new Product(1, "testName", 100.0, 10, 1, Status.AVAILABLE);
        administratorRepository.saveProduct(product);

        //when
        String input = "yes\n1\nnewName\n200\n20\n1\nno\n";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(input.getBytes());
        System.setIn(inputStream);
        administratorFunctions.modifyProductData();

        //then
        Collection<Product> products = commonRepository.loadProductFromDatabase();
        Product foundProduct = new ArrayList<>(products).get(0);
        Assertions.assertEquals("newName", foundProduct.getName());
        Assertions.assertEquals(200, foundProduct.getPrice());
        Assertions.assertEquals(20, foundProduct.getQuantity());
        Assertions.assertEquals(1, foundProduct.getId_category());
    }

    @Test
    void shouldSendOrderTest() {

        //given
        LocalDate data = LocalDate.now();
        Order order = new Order(1, 1, data, 100.0, Status.ORDERED);
        customerRepository.saveOrderToDatabase(order);

        //when
        String input = "yes\n1\nno\n";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(input.getBytes());
        System.setIn(inputStream);
        administratorFunctions.sendOrder();

        //then
        Collection<Order> orders = administratorRepository.loadOrdersFromDatabase();
        Order sendOrder = new ArrayList<>(orders).get(0);
        Assertions.assertEquals(Status.SENT, sendOrder.getStatus());
    }

    @Test
    void shouldDisplayOrdersTest() {

        //given
        LocalDate data = LocalDate.now();
        Order order = new Order(1, 1, data, 100.0, Status.ORDERED);
        Order order1 = new Order(2, 1, data, 1000.0, Status.ORDERED);
        customerRepository.saveOrderToDatabase(order);
        customerRepository.saveOrderToDatabase(order1);

        //when
        administratorFunctions.displayOrders();

        //then
        Collection<Order> orders = administratorRepository.loadOrdersFromDatabase();
        Assertions.assertEquals(2, orders.size());
        Assertions.assertTrue(orders.contains(order));
        Assertions.assertTrue(orders.contains(order1));
    }

    @Test
    void shouldDisplayCustomerTest() {

        //given
        Customer customer = new Customer(1, "name", "lastName", "email", 123, "address", "password");
        Customer customer1 = new Customer(2, "name1", "lastName1", "email1", 1234, "address1", "password1");
        customerRepository.saveCustomerToDatabase(customer);
        customerRepository.saveCustomerToDatabase(customer1);

        //when
        administratorFunctions.displayCustomers();

        //then
        Collection<Customer> customers = commonRepository.loadCustomersFromDatabase();
        Assertions.assertEquals(2, customers.size());

        System.out.println("Customer: " + customer);
        System.out.println("Customer1: " + customer1);
        Assertions.assertTrue(customers.contains(customer));
        Assertions.assertTrue(customers.contains(customer1));
    }
}
