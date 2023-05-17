package customers;

import administrator.AdministratorFunctions;
import administrator.AdministratorRepository;
import database.Database;
import products.Order;
import products.Product;
import java.time.LocalDate;
import java.util.*;

public class CustomerFunctions {
    CustomerRepository customerRepository;
    AdministratorFunctions administratorFunctions;
    AdministratorRepository administratorRepository;
    List<Product> productsInBasket = new ArrayList<>();


    public CustomerFunctions(Database database) {
        this.customerRepository = new CustomerRepository(database);
        this.administratorFunctions = new AdministratorFunctions(database);
        this.administratorRepository = new AdministratorRepository(database);

    }

    public void addProductToYourBasket() {
        Scanner scanner = new Scanner(System.in);
        String choice;

        do {
            administratorFunctions.productsInStore();
            System.out.println("Select product (id):");
            int idProduct = scanner.nextInt();
            scanner.nextLine();

            if (productExists(idProduct)) {
                Product selectedProduct = getProductById(idProduct);
                System.out.println("Quantity:");
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
        if (productsInBasket.isEmpty()) {
            System.out.println("Basket is empty");
        } else {

            System.out.println("Products:");
            for (Product product : productsInBasket) {
                System.out.println(product);

            }
            double totalPrice = calculateBasketTotalPrice();
            System.out.println("Total Price: " + totalPrice);
        }
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
                        removeProductById(id);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } while (!choice.equalsIgnoreCase("no"));
    }

    boolean productExistsInBasket(int id) {
        for (Product product : productsInBasket) {
            if (product.getId_product() == id) {
                return true;
            }
        }
        return false;
    }

    void removeProductById(int id) {
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

        do {
            System.out.println("Do you want submit order? yes/no");
            choice = scanner.nextLine();
            if (choice.equalsIgnoreCase("yes")) {
                productsInBasket();
                System.out.println("Your id:");
                int idCustomer = scanner.nextInt();
                LocalDate orderData = LocalDate.now();

                Order order = new Order(null, idCustomer, orderData, calculateBasketTotalPrice());

                customerRepository.saveOrderToDatabase(order);
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
}
