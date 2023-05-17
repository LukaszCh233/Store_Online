package administrator;

import customers.Customer;
import database.Database;
import products.Category;
import products.Order;
import products.Product;

import java.util.Collection;
import java.util.Scanner;

public class AdministratorFunctions {
    AdministratorRepository administratorRepository;


    public AdministratorFunctions(Database database) {
        this.administratorRepository = new AdministratorRepository(database);

    }

    public void createCategory() {
        Scanner scanner = new Scanner(System.in);
        String choice;
        do {
            System.out.println("New category yes/no");
            choice = scanner.nextLine();
            if (choice.equalsIgnoreCase("yes")) {
                try {
                    System.out.println("Category name:");
                    String name = scanner.nextLine();
                    Category category = new Category(null, name);
                    administratorRepository.addCategoryToDatabase(category);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        } while (!choice.equalsIgnoreCase("no"));
    }

    public void deleteCategory() {
        Scanner scanner = new Scanner(System.in);
        String choice;
        do {
            System.out.println("Delete category yes/no");
            choice = scanner.nextLine();
            if (choice.equalsIgnoreCase("yes")) {
                displayCategories();
                try {
                    System.out.println("Id category: ");
                    int delete = scanner.nextInt();
                    scanner.nextLine();
                    administratorRepository.removeCategoryFromDatabase(delete);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } while (!choice.equalsIgnoreCase("no"));
    }

    public void displayCategories() {
        Collection<Category> categories = administratorRepository.loadCategoriesFromDatabase();
        System.out.println("Categories:");
        for (Category category : categories) {
            System.out.println(category);
        }
    }

    public void addProductToStore() {
        Scanner scanner = new Scanner(System.in);
        String choice;
        do {
            try {
                System.out.println("Name: ");
                String name = scanner.nextLine();

                System.out.println("Price: ");
                while (!scanner.hasNextDouble()) {
                    System.out.println("Give a number.");
                    scanner.next();
                }
                double price = scanner.nextDouble();

                System.out.println("Quantity:");
                while (!scanner.hasNextInt()) {
                    System.out.println("Give a number");
                    scanner.next();
                }
                int quantity = scanner.nextInt();

                boolean correctCategory = false;
                while (!correctCategory) {
                    displayCategories();
                    int id_category = scanner.nextInt();
                    scanner.nextLine();
                    if (categoryExists(id_category)) {
                        Product product = new Product(null, name, price, quantity, id_category);
                        administratorRepository.saveProduct(product);
                        correctCategory = true;
                    } else {
                        System.out.println("Category does not exist");
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
            System.out.println("Back: yes/no");
            choice = scanner.nextLine();
        } while (!choice.equalsIgnoreCase("yes"));
    }

    public void displayProductsInStore() {
        Collection<Product> products = administratorRepository.loadProduct();
        System.out.println("Products in store");
        for (Product product : products) {
            System.out.println(product);
        }
    }

    public void productsInStore() {
        Scanner scanner = new Scanner(System.in);
        int choice_category;
        boolean bad_choice = false;
        while (!bad_choice) {
            displayCategories();
            choice_category = scanner.nextInt();
            Collection<Product> products = administratorRepository.loadSelectedCategoryProduct(choice_category);
            if (products.isEmpty()) {
                System.out.println("There are no products\n");
            }
            System.out.println("Category products:");
            for (Product product : products) {
                System.out.println(product);
            }
            bad_choice = true;
        }
    }

    public void deleteProduct() {
        Scanner scanner = new Scanner(System.in);
        String choice;
        do {
            System.out.println("Delete product yes/no");
            choice = scanner.nextLine();
            if (choice.equalsIgnoreCase("yes")) {
                displayProductsInStore();
                try {
                    System.out.println("Id product: ");
                    while (!scanner.hasNextInt()) {
                        System.out.println("Bad type try again");
                        scanner.next();
                    }
                    int id_product = scanner.nextInt();
                    scanner.nextLine();
                    administratorRepository.removeProductFromDatabase(id_product);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } while (!choice.equalsIgnoreCase("no"));
    }

    public void modifyProductData() {
        Scanner scanner = new Scanner(System.in);
        String choice;
        do {
            System.out.println("Modify product yes/no");
            choice = scanner.nextLine();
            if (choice.equalsIgnoreCase("yes")) {
                displayProductsInStore();
                try {
                    System.out.println("Id product:");
                    while (!scanner.hasNextInt()) {
                        System.out.println("Bad type try again");
                        scanner.next();
                    }
                    int id_product = scanner.nextInt();
                    scanner.nextLine();

                    System.out.println("Enter the new name:");
                    String name = scanner.nextLine();

                    System.out.println("Enter the new price:");
                    while (!scanner.hasNextDouble()) {
                        System.out.println("Give a number.");
                        scanner.next();
                    }
                    double price = scanner.nextDouble();

                    System.out.println("Enter the new quantity:");
                    while (!scanner.hasNextInt()) {
                        System.out.println("Give a number");
                        scanner.next();
                    }
                    int quantity = scanner.nextInt();

                    System.out.println("Enter the new category ID:");
                    boolean correctCategory = false;
                    while (!correctCategory) {
                        displayCategories();
                        int id_category = scanner.nextInt();
                        scanner.nextLine();
                        if (categoryExists(id_category)) {
                            administratorRepository.modifyDatabaseColumn(id_product, name, price, quantity, id_category);
                            correctCategory = true;
                        } else {
                            System.out.println("Category does not exist");
                        }
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } while (!choice.equalsIgnoreCase("no"));
    }

    public void displayOrders() {
        Collection<Order> orders = administratorRepository.loadOrders();
        for (Order order : orders) {
            System.out.println(order);
        }
    }

    public void displayCustomers() {
        Collection<Customer> customers = administratorRepository.loadCustomersFromDatabase();
        for (Customer customer : customers) {
            System.out.println(customer);
        }
    }

    private boolean categoryExists(int id_category) {
        Collection<Category> categories = administratorRepository.loadCategoriesFromDatabase();
        for (Category category : categories) {
            if (category.getId_category() == id_category) {
                return true;
            }
        }
        return false;
    }
}


