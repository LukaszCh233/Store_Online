package administratorTest;

import administrator.AdministratorRepository;
import common.CommonRepository;
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
import java.io.File;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;

public class AdministratorRepositoryTest {
    private static final String DATABASE_NAME = "test";
    Database database;
    AdministratorRepository administratorRepository;
    CommonRepository commonRepository;
    CustomerRepository customerRepository;

    @BeforeEach
    void createDatabase() throws SQLException {
        database = new Database(DATABASE_NAME);
        administratorRepository = new AdministratorRepository(database);
        commonRepository = new CommonRepository(database);
        customerRepository = new CustomerRepository(database);

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
    void shouldSaveCategoryToDatabaseTest() {

        // given
        Category category = new Category(1, "sport");

        // when
        administratorRepository.saveCategoryToDatabase(category);

        // then
        Collection<Category> categories = commonRepository.loadCategoriesFromDatabase();
        Assertions.assertEquals(1, categories.size());
        Assertions.assertTrue(categories.contains(category));
    }

    @Test
    void shouldSaveProductToDatabaseTest() {

        // given
        Product product = new Product(null, "ball", 200.0, 10, 1, Status.AVAILABLE);

        // when
        administratorRepository.saveProduct(product);

        // then
        Collection<Product> products = commonRepository.loadProduct();
        Product foundProduct = new ArrayList<>(products).get(0);
        Assertions.assertEquals(1, products.size());
        Assertions.assertEquals("ball", foundProduct.getName());
        Assertions.assertEquals(200.0, foundProduct.getPrice());
        Assertions.assertEquals(10, foundProduct.getQuantity());
        Assertions.assertEquals(Status.AVAILABLE, foundProduct.getStatus());
    }
    @Test
    void shouldLoadOrdersFromDatabaseTest() {

        // given
        LocalDate data = LocalDate.now();
        Order order = new Order(1, 1, data, 100.0, Status.ORDERED);
        Order order1 = new Order(2, 2, data, 1000.0, Status.ORDERED);
        customerRepository.saveOrderToDatabase(order);
        customerRepository.saveOrderToDatabase(order1);

        // when
        Collection<Order> orders = administratorRepository.loadOrders();

        // then
        Assertions.assertEquals(2, orders.size());
        Assertions.assertTrue(orders.contains(order));
        Assertions.assertTrue(orders.contains(order1));
    }
    @Test
    void shouldRemoveProductFromDatabaseTest() {

        // given
        Product product = new Product(1, "ball", 200.0, 10, 1, Status.AVAILABLE);
        Product product1 = new Product(2, "helmet", 100.0, 10, 1, Status.AVAILABLE);

        // when
        administratorRepository.saveProduct(product);
        administratorRepository.saveProduct(product1);
        administratorRepository.removeProductFromDatabase(product.getId_product());

        // then
        Collection<Product> removeProduct = commonRepository.loadProduct();
        Assertions.assertFalse(removeProduct.contains(product));
        Assertions.assertTrue(removeProduct.contains(product1));
    }
    @Test
    void shouldRemoveCategoryFromDatabaseTest() {

        // given
        Category category = new Category(1, "sport");
        Category category1 = new Category(2, "food");

        // when
        administratorRepository.saveCategoryToDatabase(category);
        administratorRepository.saveCategoryToDatabase(category1);
        administratorRepository.removeCategoryFromDatabase(category1.getId_category());

        // then
        Collection<Category> categories = commonRepository.loadCategoriesFromDatabase();
        Assertions.assertFalse(categories.contains(category1));
        Assertions.assertTrue(categories.contains(category));
    }
    @Test
    void shouldModifyProductsColumnTest() {

        // given
        Product product = new Product(1, "ball", 200.0, 10, 1, Status.AVAILABLE);

        // when
        administratorRepository.saveProduct(product);
        administratorRepository.modifyProductsColumn(1, "newBall", 300.0, 5, 1);

        // then
        Collection<Product> products = commonRepository.loadProduct();
        Product modifyProduct = new ArrayList<>(products).get(0);
        Assertions.assertEquals("newBall", modifyProduct.getName());
        Assertions.assertEquals(300.0, modifyProduct.getPrice());
        Assertions.assertEquals(5, modifyProduct.getQuantity());
    }
    @Test
    void shouldUpdateOrdersStatusTest() {

        // given
        LocalDate data = LocalDate.now();
        Order order = new Order(1, 1, data, 100.0, Status.ORDERED);
        customerRepository.saveOrderToDatabase(order);

        // when
        administratorRepository.updateOrderStatus(order.getIdOrder(), Status.SENT);

        // then
        Collection<Order> orders = administratorRepository.loadOrders();
        Order updateStatus = new ArrayList<>(orders).get(0);
        Assertions.assertEquals(Status.SENT, updateStatus.getStatus());
    }
}
