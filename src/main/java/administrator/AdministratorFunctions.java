package administrator;

import common.CommonFunctions;
import common.CommonRepository;
import customers.Customer;
import database.Database;
import products.Category;
import products.Order;
import products.Product;
import products.Status;
import java.util.Collection;
import java.util.Scanner;

public class AdministratorFunctions {
    AdministratorRepository administratorRepository;
    CommonRepository commonRepository;
    CommonFunctions commonFunctions;

    public AdministratorFunctions(Database database) {
        this.administratorRepository = new AdministratorRepository(database);
        this.commonRepository = new CommonRepository(database);
        this.commonFunctions = new CommonFunctions(database);
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
                commonFunctions.displayCategories();
                try {
                    System.out.println("Id category: ");
                    while (!scanner.hasNextInt()) {
                        System.out.println("Give a number");
                        scanner.next();
                    }
                    int delete = scanner.nextInt();
                    scanner.nextLine();
                    administratorRepository.removeCategoryFromDatabase(delete);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } while (!choice.equalsIgnoreCase("no"));
    }

    public void addProductToStore() {
        Scanner scanner = new Scanner(System.in);
        String choice;
        if (!categoriesExists()) {
            return;
        }
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
                    commonFunctions.displayCategories();
                    while (!scanner.hasNextDouble()) {
                        System.out.println("Give a number.");
                        scanner.next();
                    }
                    int id_category = scanner.nextInt();
                    scanner.nextLine();
                    if (categoryExists(id_category)) {
                        Product product = new Product(null, name, price, quantity, id_category, Status.AVAILABLE);
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

    public void deleteProduct() {
        Scanner scanner = new Scanner(System.in);
        String choice;
        do {
            System.out.println("Delete product yes/no");
            choice = scanner.nextLine();
            if (choice.equalsIgnoreCase("yes")) {
                commonFunctions.displayProductsInStore();
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
                commonFunctions.displayProductsInStore();
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
                        commonFunctions.displayCategories();
                        while (!scanner.hasNextInt()) {
                            System.out.println("Give a number");
                            scanner.next();
                        }
                        int id_category = scanner.nextInt();
                        scanner.nextLine();
                        if (categoryExists(id_category)) {
                            administratorRepository.modifyProductsColumn(id_product, name, price, quantity, id_category);
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

    public void sendOrder() {
        Scanner scanner = new Scanner(System.in);
        String choice;

        do {
            System.out.println("Send order yes/no");
            choice = scanner.nextLine();
            if (choice.equalsIgnoreCase("yes")) {
                displayOrders();
                try {
                    System.out.println("Id product: ");
                    while (!scanner.hasNextInt()) {
                        System.out.println("Bad type try again");
                        scanner.next();
                    }
                    int idOrders = scanner.nextInt();
                    scanner.nextLine();
                    administratorRepository.updateOrderStatus(idOrders, Status.SENT);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        } while (!choice.equalsIgnoreCase("yes"));
    }

    public void displayOrders() {
        Collection<Order> orders = administratorRepository.loadOrders();
        for (Order order : orders) {
            System.out.println(order.toStringForAdministrator());
        }
    }

    public void displayCustomers() {
        Collection<Customer> customers = commonRepository.loadCustomersFromDatabase();
        for (Customer customer : customers) {
            System.out.println(customer.toStringForAdministrator());
        }
    }

    private boolean categoryExists(int id_category) {
        Collection<Category> categories = commonRepository.loadCategoriesFromDatabase();
        for (Category category : categories) {
            if (category.getId_category() == id_category) {
                return true;
            }
        }
        return false;
    }

    private boolean categoriesExists() {
        Collection<Category> categories = commonRepository.loadCategoriesFromDatabase();
        if (categories.isEmpty()) {
            System.out.println("First create category");
            return false;
        }
        return true;
    }
}


