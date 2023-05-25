package customers;

import administrator.AdministratorFunctions;
import administrator.AdministratorRepository;
import common.CommonFunctions;
import common.CommonRepository;
import database.Database;
import products.Order;
import products.Product;

import java.time.LocalDate;
import java.util.*;

public class CustomerFunctions {
    CustomerRepository customerRepository;
    AdministratorFunctions administratorFunctions;
    AdministratorRepository administratorRepository;
    CommonRepository commonRepository;
    CommonFunctions commonFunctions;
    Collection<Product> productsInBasket = new ArrayList<>();


    public CustomerFunctions(Database database) {
        this.customerRepository = new CustomerRepository(database);
        this.administratorFunctions = new AdministratorFunctions(database);
        this.administratorRepository = new AdministratorRepository(database);
        this.commonRepository = new CommonRepository(database);
        this.commonFunctions = new CommonFunctions(database);

    }

    public void addProductToYourBasket() {
        Scanner scanner = new Scanner(System.in);
        String choice;

        do {
            commonFunctions.productsInStore();
            while (!scanner.hasNextInt()) {
                System.out.println("Give a number");
                scanner.next();
            }
            int idProduct = scanner.nextInt();
            scanner.nextLine();
            if (productExists(idProduct)) {
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
                    selectedProduct.setSelectedQuantity(quantity);
                    selectedProduct.setQuantity(selectedProduct.getQuantity() - quantity);
                    productsInBasket.add(selectedProduct);
                    customerRepository.updateProductQuantityInDatabase(idProduct, selectedProduct.getQuantity());
                    System.out.println("Product added to basket");
                }
            } else {
                System.out.println("Bad id, try again");
            }
            System.out.println("Back to menu yes/no");
            choice = scanner.nextLine();
        } while (!choice.equalsIgnoreCase("yes"));
    }

    public void productsInBasket() {

        System.out.println("Products:");
        for (Product product : productsInBasket) {

            System.out.println(product.toStringForBasket());
        }
        double totalPrice = calculateBasketTotalPrice();
        System.out.println("Total Price: " + totalPrice);
    }

    public double calculateBasketTotalPrice() {
        double totalPrice = 0.0;
        for (Product product : productsInBasket) {
            totalPrice = totalPrice + product.getPrice() * product.getSelectedQuantity();
        }
        return totalPrice;
    }

    public void removeProductFromBasket() {
        Scanner scanner = new Scanner(System.in);
        String choice;
        if (productsInBasket.isEmpty()) {
            System.out.println("Basket is empty");
            return;
        }
        do {
            System.out.println("Remove product yes/no");
            choice = scanner.nextLine();
            if (choice.equalsIgnoreCase("yes")) {
                productsInBasket();
                try {
                    System.out.println("Id product: ");
                    int id = scanner.nextInt();
                    scanner.nextLine();
                    if (productExistsInBasket(id)) {
                        int quantityInBasket = getProductQuantityInBasket(id);
                        Product removeProduct = getProductById(id);
                        System.out.println("Quantity:");
                        int quantity = scanner.nextInt();
                        removeProduct.setSelectedQuantity(quantity);

                        if (quantity < removeProduct.getSelectedQuantity()) {
                            removeProduct.setSelectedQuantity(removeProduct.getSelectedQuantity() - quantity);
                            removeProduct.setQuantity(removeProduct.getQuantity() + quantity);
                            customerRepository.updateProductQuantityInDatabase(id, removeProduct.getQuantity());
                        } else {
                            removeProduct.setQuantity(removeProduct.getQuantity() + quantity);
                            removeProductById(id);
                            customerRepository.updateProductQuantityInDatabase(id, removeProduct.getQuantity());
                            System.out.println("Product delete");
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } while (!choice.equalsIgnoreCase("no"));
    }

    public int getProductQuantityInBasket(int productId) {
        for (Product product : productsInBasket) {
            if (product.getId_product() == productId) {
                return product.getSelectedQuantity();
            }
        }
        return 0;
    }

    boolean productExistsInBasket(int id) {
        for (Product product : productsInBasket) {
            if (product.getId_product() == id) {
                return true;
            }
        }
        return false;
    }

    public void removeProductById(int id) {
        Iterator<Product> iterator = productsInBasket.iterator();
        while (iterator.hasNext()) {
            Product product = iterator.next();
            if (product.getId_product() == id) {
                iterator.remove();
                break;
            }
        }
    }

    public void submitOrder() {
        Scanner scanner = new Scanner(System.in);
        String choice;

        if (productsInBasket.isEmpty()) {
            System.out.println("Basket is empty");
            return;
        }

        do {
            System.out.println("Do you want submit order? yes/no");
            choice = scanner.nextLine();
            if (choice.equalsIgnoreCase("yes")) {
                productsInBasket();
                System.out.println("Your email:");
                String email = scanner.nextLine();
                int idCustomer = customerRepository.getCustomerIdByEmail(email);
                if (idCustomer != -1) {
                    LocalDate orderData = LocalDate.now();

                    Order order = new Order(null, idCustomer, orderData, calculateBasketTotalPrice());

                    customerRepository.saveOrderToDatabase(order);
                    productsInBasket.clear();
                }
            }

        } while (!choice.equalsIgnoreCase("no"));
    }

    public boolean productExists(int idProduct) {
        Collection<Product> products = administratorRepository.loadProduct();
        for (Product product : products) {
            if (product.getId_product() == idProduct) {
                return true;
            }
        }
        return false;
    }

    public Product getProductById(int id) {
        Collection<Product> products = administratorRepository.loadProduct();
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

    public void displayCustomerData(String loggedInEmail) {
        int customerId = customerRepository.getCustomerIdByEmail(loggedInEmail);
        if (customerId != -1) {
            Customer loggedCustomer = getCustomerById(customerId);
            if (loggedCustomer != null) {
                System.out.println(loggedCustomer.toStringForCustomer());
            } else {
                System.out.println("Customer not found.");
            }
        } else {
            System.out.println("Customer not found.");
        }
    }
}


