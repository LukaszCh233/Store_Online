package common;

import administrator.AdministratorRepository;
import database.Database;
import products.Category;
import products.Product;
import products.Status;

import java.util.Collection;
import java.util.Scanner;

public class CommonFunctions {
    AdministratorRepository administratorRepository;
    CommonRepository commonRepository;



    public CommonFunctions(Database database) {
        this.administratorRepository = new AdministratorRepository(database);
        this.commonRepository = new CommonRepository(database);

    }
    public void productsInStore() {
        Scanner scanner = new Scanner(System.in);
        int choice_category;

        boolean bad_choice = false;
        while (!bad_choice) {
            displayCategories();
            while (!scanner.hasNextInt()) {
                System.out.println("Give a number");
                scanner.next();
            }
            choice_category = scanner.nextInt();
            Collection<Product> products = administratorRepository.loadSelectedCategoryProduct(choice_category);
            if (products.isEmpty()) {
                System.out.println("There are no products");
                continue;
            } else {
                System.out.println("Category products:");
                for (Product product : products) {
                    if (product.getQuantity() == 0) {
                        product.setStatus(Status.LACK);
                    }
                    System.out.println(product.toStringForStore());
                }
            }
            bad_choice = true;
        }
    }
    public void displayCategories() {
        Collection<Category> categories = commonRepository.loadCategoriesFromDatabase();
        System.out.println("Categories:");
        for (Category category : categories) {
            System.out.println(category);
        }
    }
    public void displayProductsInStore() {
        Collection<Product> products = commonRepository.loadProduct();
        System.out.println("Products in store");
        for (Product product : products) {
            if(product.getQuantity() == 0) {
                product.setStatus(Status.LACK);
            }
            System.out.println(product.toStringForStore());
        }
    }
}
