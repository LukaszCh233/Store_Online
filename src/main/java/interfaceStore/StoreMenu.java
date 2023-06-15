package interfaceStore;

import administrator.AdministratorFunctions;
import common.CommonFunctions;
import customers.CustomerFunctions;
import customers.CustomerRepository;
import database.Database;

import java.sql.SQLException;
import java.util.Scanner;

public class StoreMenu implements AutoCloseable {
    Database database;
    AdministratorFunctions administratorFunctions;
    CustomerFunctions customerFunctions;
    CustomerRepository customerRepository;
    CommonFunctions commonFunctions;

    public StoreMenu() throws SQLException {
        database = new Database();
        administratorFunctions = new AdministratorFunctions(database);
        customerFunctions = new CustomerFunctions(database);
        customerRepository = new CustomerRepository(database);
        commonFunctions = new CommonFunctions(database);
    }

    public void interfaceChoice() {
        Scanner scanner = new Scanner(System.in);
        String choice;
        while (true) {
            System.out.println(" 1- admin\n 2- customer");
            choice = scanner.nextLine();
            if (choice.equalsIgnoreCase("1")) {
                administratorInterface();
                break;
            } else if (choice.equalsIgnoreCase("2")) {
                loginOptions();
                break;
            } else System.out.println("Please choose 1 or 2");
        }
    }

    public void loginOptions() {
        Scanner scanner = new Scanner(System.in);
        int choice;
        boolean correct = false;
        while (!correct) {
            System.out.println("Login - 1\nRegister - 2");
            while (!scanner.hasNextInt()) {
                System.out.println("Give a number");
                scanner.next();
            }
            choice = scanner.nextInt();
            if (choice == 1) {
                customerFunctions.loginCustomer();
                correct = true;
                customerInterface();
            } else if (choice == 2) {
                customerFunctions.registerCustomer();
                correct = true;
            } else System.out.println("Try again");
        }
    }

    public void administratorInterface() {

        Scanner scanner = new Scanner(System.in);
        String choice;

        do {
            System.out.println("1: Add product to store");
            System.out.println("2: Delete product from store");
            System.out.println("3: Products in store");
            System.out.println("4: Modify product in store");
            System.out.println("5: Orders");
            System.out.println("6: Customers");
            System.out.println("7: Create category");
            System.out.println("8: Categories");
            System.out.println("9: Products from selected category");
            System.out.println("10: Delete category");
            System.out.println("11: Order management");
            System.out.println("0: Close program");
            choice = scanner.nextLine();
            switch (choice) {
                case "1" -> administratorFunctions.addProductToStore();
                case "2" -> administratorFunctions.deleteProductFromStore();
                case "3" -> commonFunctions.displayCategoryProductsInStore();
                case "4" -> administratorFunctions.modifyProductData();
                case "5" -> administratorFunctions.displayOrders();
                case "6" -> administratorFunctions.displayCustomers();
                case "7" -> administratorFunctions.createCategory();
                case "8" -> commonFunctions.displayCategories();
                case "9" -> commonFunctions.displayProductsInStore();
                case "10" -> administratorFunctions.deleteCategory();
                case "11" -> administratorFunctions.sendOrder();
                case "0" -> System.out.println("EXIT");
                default -> System.out.println("Bad choice, try again");
            }

        } while (!choice.equalsIgnoreCase("0"));
    }

    public void customerInterface() {
        Scanner scanner = new Scanner(System.in);
        String choice;
        do {
            System.out.println("1: Add product to basket");
            System.out.println("2: Delete product from basket");
            System.out.println("3: Basket");
            System.out.println("4: Submit your order");
            System.out.println("5: History of orders");
            System.out.println("6: Account data");
            System.out.println("7: Update account data");
            System.out.println("0: Close program");
            choice = scanner.nextLine();
            switch (choice) {
                case "1" -> customerFunctions.addProductToBasket();
                case "2" -> customerFunctions.removeProductFromBasket();
                case "3" -> customerFunctions.displayProductsInBasket();
                case "4" -> customerFunctions.submitOrder();
                case "5" -> customerFunctions.displayOrdersHistory(customerFunctions.loggedInEmail);
                case "6" -> customerFunctions.displayCustomerData(customerFunctions.loggedInEmail);
                case "7" -> customerFunctions.changeAccountData(customerFunctions.loggedInEmail);
                case "0" -> customerFunctions.returnProductsAndExit();
                default -> System.out.println("Bad choice, try again");
            }
        } while (!choice.equalsIgnoreCase("0"));
    }

    @Override
    public void close() throws Exception {
        database.close();
    }
}

