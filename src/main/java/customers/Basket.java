package customers;

import lombok.Getter;
import products.Product;

import java.util.ArrayList;
import java.util.List;
@Getter
public class Basket {
    Integer id_product;
    String name;
    Double price;
    int quantity;
    List<Product> basket = new ArrayList<>();

    public Basket(Integer id_product, String name, Double price, int quantity) {
        this.id_product = id_product;
        this.name = name;
        this.price = price;
        this.quantity = quantity;
    }

    public Basket(List<Product> basket) {
        this.basket = basket;
    }

    @Override
    public String toString() {
        return
                "id: " + id_product +
                        " | name: " + name +
                        " | price: " + price +"zl" +
                        " | quantity: " + quantity +
                        "\n---------------------------------------------------";
    }
}
