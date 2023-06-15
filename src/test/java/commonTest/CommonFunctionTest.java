package commonTest;

import administrator.AdministratorRepository;
import common.CommonFunctions;
import common.CommonRepository;
import customers.CustomerFunctions;
import customers.CustomerRepository;
import database.Database;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import products.Category;
import products.Product;
import products.Status;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.sql.SQLException;
import java.util.Collection;

public class CommonFunctionTest {
    private static final String DATABASE_NAME = "test";
    Database database;
    AdministratorRepository administratorRepository;
    CommonRepository commonRepository;
    CommonFunctions commonFunctions;
    CustomerRepository customerRepository;
    CustomerFunctions customerFunctions;

    @BeforeEach
    void createDatabase() throws SQLException {
        database = new Database(DATABASE_NAME);
        administratorRepository = new AdministratorRepository(database);
        commonRepository = new CommonRepository(database);
        customerRepository = new CustomerRepository(database);
        customerFunctions = new CustomerFunctions(database);
        commonFunctions = new CommonFunctions(database);

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
    void shouldDisplayCategoryProductInStoreTest() {

        //given
        Category category = new Category(1, "category1");
        Product product = new Product(1, "ball", 200.0, 10, 1, Status.AVAILABLE);
        Product product1 = new Product(2, "hat", 1200.0, 10, 1, Status.AVAILABLE);
        administratorRepository.saveCategoryToDatabase(category);
        administratorRepository.saveProduct(product);
        administratorRepository.saveProduct(product1);

        //when
        String input = "1\n";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(input.getBytes());
        System.setIn(inputStream);
        commonFunctions.displayCategoryProductsInStore();

        //then
        Collection<Product> products = commonRepository.loadSelectedCategoryProduct(1);
        Assertions.assertEquals(2, products.size());
        Assertions.assertTrue(products.contains(product));
        Assertions.assertTrue(products.contains(product1));
    }
    @Test
    void shouldDisplayProductInStoreTest() {

        //given
        Product product = new Product(1, "ball", 200.0, 10, 1, Status.AVAILABLE);
        Product product1 = new Product(2, "hat", 1200.0, 10, 1, Status.AVAILABLE);
        administratorRepository.saveProduct(product);
        administratorRepository.saveProduct(product1);

        //when
        commonFunctions.displayProductsInStore();

        //then
        Collection<Product> products = commonRepository.loadProduct();
        Assertions.assertEquals(2, products.size());
        Assertions.assertTrue(products.contains(product));
        Assertions.assertTrue(products.contains(product1));
    }
    @Test
    void shouldDisplayCategoriesTest() {

        //given
        Category category = new Category(1, "category1");
        Category category1 = new Category(2, "category2");
        administratorRepository.saveCategoryToDatabase(category);
        administratorRepository.saveCategoryToDatabase(category1);

        //when
        commonFunctions.displayCategories();

        //then
        Collection<Category> categories = commonRepository.loadCategoriesFromDatabase();
        Assertions.assertEquals(2, categories.size());
        Assertions.assertTrue(categories.contains(category));
        Assertions.assertTrue(categories.contains(category1));
    }
}
