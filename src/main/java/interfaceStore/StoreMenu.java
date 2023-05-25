package interfaceStore;

import administrator.AdministratorFunctions;
import common.CommonFunctions;
import customers.Customer;
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
    String loggedInEmail;



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
            System.out.println("Choose 1- admin 2- customer");
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
                loginCustomer();
                correct = true;
            } else if (choice == 2) {
                registerCustomer();
                correct = true;
            } else System.out.println("Try again");

        }
    }
    public void registerCustomer() {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Name:");
        String name = scanner.nextLine();

        System.out.println("Last Name:");
        String lastName = scanner.nextLine();

        System.out.println("Email:");
        String email = scanner.nextLine();

        System.out.println("Number:");
        while (!scanner.hasNextInt()) {
            System.out.println("Give a number");
            scanner.next();
        }
        int number = scanner.nextInt();
        scanner.nextLine();

        System.out.println("Address:");
        String address = scanner.nextLine();

        System.out.println("Password:");
        String password = scanner.nextLine();

        Customer customer = new Customer(null, name, lastName, email, number, address,password);

        customerRepository.saveCustomerToDatabase(customer);
        System.out.println("Register finish");
        customerInterface();
    }
    public void loginCustomer() {

        Scanner scanner = new Scanner(System.in);

        boolean loginSuccessful = false;
        while (!loginSuccessful) {
            System.out.println("Email:");
            String email = scanner.nextLine();
            System.out.println("Password:");
            String password = scanner.nextLine();
            loginSuccessful = customerRepository.login(email, password);
            if (loginSuccessful) {
                loggedInEmail = email;
                customerRepository.getCustomerIdByEmail(email);
                System.out.println("Login successful");
                customerInterface();

            } else {
                System.out.println("Try again :)");
            }
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
            System.out.println("0: Close program");
            choice = scanner.nextLine();
            switch (choice) {
                case "1": administratorFunctions.addProductToStore(); break;
                case "2": administratorFunctions.deleteProduct(); break;
                case "3": commonFunctions.displayProductsInStore(); break;
                case "4": administratorFunctions.modifyProductData(); break;
                case "5": administratorFunctions.displayOrders(); break;
                case "6": administratorFunctions.displayCustomers(); break;
                case "7": administratorFunctions.createCategory(); break;
                case "8": commonFunctions.displayCategories(); break;
                case "9": commonFunctions.productsInStore(); break;
                case "10":administratorFunctions.deleteCategory(); break;
                case "0": break;
                default: System.out.println("Bad choice, try again");

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
            System.out.println("0: Close program");
            choice = scanner.nextLine();
            switch (choice) {
                case "1": customerFunctions.addProductToYourBasket(); break;
                case "2": customerFunctions.removeProductFromBasket(); break;
                case "3": customerFunctions.productsInBasket(); break;
                case "4": customerFunctions.submitOrder(); break;
                case "5": break;
                case "6": customerFunctions.displayCustomerData(loggedInEmail); break;
                case "0": break;
                default:
                    System.out.println("Bad choice, try again");

            }
        } while (!choice.equalsIgnoreCase("0"));
    }

    @Override
    public void close() throws Exception {
        database.close();
    }
}

