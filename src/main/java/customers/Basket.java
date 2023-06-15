package customers;

import lombok.Getter;
import lombok.Setter;

import java.util.Objects;

@Getter
@Setter
public class Basket {
    Integer id_basket;
    Integer id_product;
    String name;
    Double price;
    int quantity;


    public Basket(Integer id_basket, Integer id_product, String name, Double price, int quantity) {
        this.id_basket = id_basket;
        this.id_product = id_product;
        this.name = name;
        this.price = price;
        this.quantity = quantity;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Basket basket)) return false;
        return quantity == basket.quantity && Objects.equals(id_basket, basket.id_basket) && Objects.equals(id_product, basket.id_product) && Objects.equals(name, basket.name) && Objects.equals(price, basket.price);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id_basket, id_product, name, price, quantity);
    }

    @Override
    public String toString() {
        return
                "id: " + id_product +
                        " | name: " + name +
                        " | price: " + price + "zl" +
                        " | quantity: " + quantity +
                        "\n---------------------------------------------------";
    }
}
