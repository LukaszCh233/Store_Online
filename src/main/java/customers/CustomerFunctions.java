package customers;

import common.CommonFunctions;
import common.CommonRepository;
import database.Database;
import products.Order;
import products.Product;
import products.Status;

import java.time.LocalDate;
import java.util.Collection;
import java.util.Scanner;

public class CustomerFunctions {
    public String loggedInEmail;
    CustomerRepository customerRepository;
    CommonRepository commonRepository;
    CommonFunctions commonFunctions;

    public CustomerFunctions(Database database) {
        this.customerRepository = new CustomerRepository(database);
        this.commonRepository = new CommonRepository(database);
        this.commonFunctions = new CommonFunctions(database);

    }

    public void registerCustomer() {

        Scanner scanner = new Scanner(System.in);
        String choice;
        do {
            try {
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

                Customer customer = new Customer(null, name, lastName, email, number, address, password);

                customerRepository.saveCustomerToDatabase(customer);
                System.out.println("Register finish\n Now You can login");
            } catch (Exception e) {
                e.printStackTrace();
            }
            System.out.println("Back to menu yes/no");
            choice = scanner.nextLine();

        } while (!choice.equalsIgnoreCase("yes"));

    }

    public void loginCustomer() {

        Scanner scanner = new Scanner(System.in);
        boolean loginSuccessful = false;

        while (!loginSuccessful) {
            try {
                System.out.println("Email:");
                String email = scanner.nextLine();
                System.out.println("Password:");
                String password = scanner.nextLine();

                loginSuccessful = customerRepository.login(email, password);
                if (loginSuccessful) {
                    loggedInEmail = email;
                    customerRepository.getCustomerIdByEmail(email);
                    System.out.println("Login successful");
                } else {
                    System.out.println("Try again :)");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void addProductToBasket() {
        Scanner scanner = new Scanner(System.in);
        String choice;

        do {
            try {
                commonFunctions.displayCategoryProductsInStore();
                System.out.println("Id product:");
                while (!scanner.hasNextInt()) {
                    System.out.println("Give a number");
                    scanner.next();
                }
                int idProduct = scanner.nextInt();
                scanner.nextLine();
                if (commonFunctions.productExists(idProduct)) {
                    Product selectedProduct = getProductById(idProduct);
                    System.out.println("Quantity:");
                    while (!scanner.hasNextInt()) {
                        System.out.println("Bad type try again");
                        scanner.next();
                    }
                    int quantity = scanner.nextInt();
                    scanner.nextLine();

                    if (quantity > selectedProduct.getQuantity()) {
                        System.out.println("product quantity not available");
                    } else if (quantity > 0) {
                        if (existInBasket(idProduct)) {
                            int actualQuantity = getProductQuantityInBasket(idProduct);
                            selectedProduct.setQuantity(selectedProduct.getQuantity() - quantity);
                            customerRepository.updateQuantityInBasket(idProduct, quantity + actualQuantity);
                        } else {

                            selectedProduct.setSelectedQuantity(quantity);
                            selectedProduct.setQuantity(selectedProduct.getQuantity() - quantity);
                            Basket basket = new Basket(null, idProduct, selectedProduct.getName(), selectedProduct.getPrice(), selectedProduct.getSelectedQuantity());
                            customerRepository.saveProductToBasketDatabase(basket);
                        }
                        customerRepository.updateProductQuantityInDatabase(idProduct, selectedProduct.getQuantity());
                        System.out.println("Product added to basket");
                    }
                } else {
                    System.out.println("Bad id, try again");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            System.out.println("Back to menu yes/no");
            choice = scanner.nextLine();
        } while (!choice.equalsIgnoreCase("yes"));
    }

    public void displayProductsInBasket() {
        Collection<Basket> basket = customerRepository.loadBasketFromDatabase();
        for (Basket basketProduct : basket) {
            System.out.println(basketProduct);
        }
        double totalPrice = calculateBasketTotalPrice();
        System.out.println("Total Price: " + totalPrice);
    }

    public double calculateBasketTotalPrice() {
        Collection<Basket> basket = customerRepository.loadBasketFromDatabase();
        double totalPrice = 0.0;
        for (Basket basketProduct : basket) {
            totalPrice = totalPrice + basketProduct.getPrice() * basketProduct.getQuantity();
        }
        return totalPrice;
    }

    public void removeProductFromBasket() {
        Scanner scanner = new Scanner(System.in);
        String choice;

        do {
            System.out.println("Remove product yes/no");
            choice = scanner.nextLine();
            if (!checkBasket()) {
                continue;
            }
            if (choice.equalsIgnoreCase("yes")) {
                displayProductsInBasket();
                try {
                    System.out.println("Id product: ");
                    int id = scanner.nextInt();
                    scanner.nextLine();
                    if (!existInBasket(idProduct)) {
                        System.out.println("Product does not exist ");
                        continue;
                    }
                    System.out.println("Quantity:");
                    while (!scanner.hasNextInt()) {
                        System.out.println("Bad type try again");
                        scanner.next();
                    }
                    int quantity = scanner.nextInt();
                    scanner.nextLine();
                    Product selectedProduct = getProductById(idProduct);
                    int actualQuantity = getProductQuantityInBasket(idProduct) - quantity;


                    selectedProduct.setQuantity(selectedProduct.getQuantity() + quantity);
                    customerRepository.updateProductQuantityInDatabase(idProduct, selectedProduct.getQuantity());
                    customerRepository.updateQuantityInBasket(idProduct, actualQuantity);
                    if (actualQuantity <= 0) {
                        customerRepository.removeProductFromBasketDatabase(idProduct);
                        System.out.println("Product deleted");
                    }


                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } while (!choice.equalsIgnoreCase("no"));
    }

    public int getProductQuantityInBasket(int productId) {
        Collection<Basket> basket = customerRepository.loadBasketFromDatabase();
        for (Basket product : basket) {
            if (product.getId_product() == productId) {
                return product.getQuantity();
            }
        }
        return 0;
    }

    private boolean existInBasket(int productId) {
        Collection<Basket> basket = customerRepository.loadBasketFromDatabase();
        for (Basket product : basket) {
            if (product.getId_product() == productId) {
                return true;
            }
        }
        return false;
    }


    public void submitOrder() {
        Scanner scanner = new Scanner(System.in);
        Collection<Basket> basket = customerRepository.loadBasketFromDatabase();
        if (basket.isEmpty()) {
            System.out.println("Your basket is empty\n");
            return;
        }
        while (!basket.isEmpty()) {
            displayProductsInBasket();
            System.out.println("Your email:");
            String email = scanner.nextLine();
            int idCustomer = customerRepository.getCustomerIdByEmail(email);
            if (idCustomer != -1) {
                LocalDate orderData = LocalDate.now();
                Order order = new Order(null, idCustomer, orderData, calculateBasketTotalPrice(), Status.ORDERED);
                customerRepository.saveOrderToDatabase(order);
                customerRepository.clearBasketDatabase();
                System.out.println("Ordered");
                break;
            } else System.out.println("Bad email");
        }
    }

    public void changeAccountData(String loggedEmail) {
        Scanner scanner = new Scanner(System.in);
        String choice;
        int customerId = customerRepository.getCustomerIdByEmail(loggedEmail);

        do {
            System.out.println("Modify data yes/no");
            choice = scanner.nextLine();
            if (choice.equalsIgnoreCase("yes")) {
                displayCustomerData(loggedEmail);
                try {
                    System.out.println("Modify:\n 1 - password and email\n 2 - basic data");
                    int whatModify = scanner.nextInt();
                    scanner.nextLine();
                    if (whatModify == 2) {
                        System.out.println("Name:");
                        String name = scanner.nextLine();

                        System.out.println("LastName:");
                        String lastName = scanner.nextLine();

                        System.out.println("Number:");
                        while (!scanner.hasNextInt()) {
                            System.out.println("Give a number");
                            scanner.next();
                        }
                        int number = scanner.nextInt();
                        scanner.nextLine();

                        System.out.println("Address:");
                        String address = scanner.nextLine();

                        customerRepository.modifyCustomersColumnInDatabase(customerId, name, lastName, number, address);
                    } else if (whatModify == 1) {
                        System.out.println("Email:");
                        String email = scanner.nextLine();

                        System.out.println("Password:");
                        String password = scanner.nextLine();

                        customerRepository.modifyCustomersPasswordColumnInDatabase(customerId, email, password);
                        System.out.println("Now please log in again\n");
                    } else System.out.println("Try again");

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        } while (!choice.equalsIgnoreCase("yes"));
    }

    public Product getProductById(int id) {
        Collection<Product> products = commonRepository.loadProduct();
        for (Product product : products) {
            if (product.getId_product() == id) {
                return product;
            }
        }
        return null;
    }

    public Customer getCustomerById(int id) {
        Collection<Customer> customers = commonRepository.loadCustomersFromDatabase();
        for (Customer customer : customers) {
            if (customer.getId_customer() == id) {
                return customer;
            }
        }
        return null;
    }

    public Order getOrdersById(int id) {
        Collection<Order> allOrders = customerRepository.loadOrders(id);

        for (Order order : allOrders) {
            if (order.getIdCustomer() == id) {
                System.out.println(order.toStringForCustomer());
            }
        }
        return null;
    }

    public void displayCustomerData(String loggedInEmail) {
        int customerId = customerRepository.getCustomerIdByEmail(loggedInEmail);
        if (customerId != -1) {
            Customer loggedCustomer = getCustomerById(customerId);
            if (loggedCustomer != null) {
                System.out.println(loggedCustomer);
            } else {
                System.out.println("Customer not found.");
            }
        } else {
            System.out.println("Customer not found.");
        }
    }

    public void displayOrdersHistory(String loggedEmail) {

        int customerId = customerRepository.getCustomerIdByEmail(loggedEmail);
        if (customerId != -1) {
            Order loggedCustomer = getOrdersById(customerId);
            if (loggedCustomer != null) {
                System.out.println(loggedCustomer.toStringForCustomer());
            }
        } else {
            System.out.println("Customer not found.");
        }
    }

    public void returnProductsAndExit() {
        Collection<Basket> basket = customerRepository.loadBasketFromDatabase();

        for (Basket basketItem : basket) {
            int productId = basketItem.getId_product();
            Product selectedProduct = getProductById(productId);
            int quantity = basketItem.getQuantity();
            selectedProduct.setQuantity(selectedProduct.getQuantity() + quantity);
            customerRepository.updateProductQuantityInDatabase(productId, selectedProduct.getQuantity());
        }
        customerRepository.clearBasketDatabase();
    }

    private boolean checkBasket() {
        Collection<Basket> basket = customerRepository.loadBasketFromDatabase();
        if (basket.isEmpty()) {
            System.out.println("Basket is empty");
            return false;
        }
        return true;
    }
}




