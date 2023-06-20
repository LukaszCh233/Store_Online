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

    public void displayCategoryProductsInStore() {
        Scanner scanner = new Scanner(System.in);

        boolean bad_choice = false;
        while (!bad_choice) {
            try {
                displayCategories();
                while (!scanner.hasNextInt()) {
                    System.out.println("Give a number");
                    scanner.next();
                }
                int choiceCategory = scanner.nextInt();


                Collection<Product> productsInCategory = commonRepository.loadSelectedCategoryProduct(choiceCategory);
                if (!categoryExist(choiceCategory)) {
                    System.out.println("there is no such category");
                    continue;
                }
                if (productsInCategory.isEmpty()) {
                    System.out.println("There are no products");
                    continue;
                } else {
                    System.out.println("Category products:");
                    for (Product product : productsInCategory) {
                        if (product.getQuantity() == 0) {
                            product.setStatus(Status.LACK);
                        }
                        System.out.println(product);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();

            }
            bad_choice = true;
        }
    }

    private boolean categoryExist(int idCategory) {
        Collection<Category> categories = commonRepository.loadCategoriesFromDatabase();
        for (Category category : categories) {
            if (category.getId_category() == idCategory) {
                return true;
            }
        }
        return false;
    }

    public void displayCategories() {
        Collection<Category> categories = commonRepository.loadCategoriesFromDatabase();
        System.out.println("Categories:");
        for (Category category : categories) {
            System.out.println(category);
        }
    }

    public void displayProductsInStore() {
        Collection<Product> products = commonRepository.loadProductFromDatabase();
        System.out.println("Products in store");
        for (Product product : products) {
            if (product.getQuantity() == 0) {
                product.setStatus(Status.LACK);
            }
            System.out.println(product);
        }
    }

    public boolean productExists(int idProduct) {
        Collection<Product> products = commonRepository.loadProductFromDatabase();
        for (Product product : products) {
            if (product.getId_product() == idProduct) {
                return true;
            }
        }
        return false;
    }
}
