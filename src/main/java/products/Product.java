package products;

import lombok.Getter;
import lombok.Setter;

import java.util.Objects;

@Getter
@Setter
public class Product {
    private Integer id_product;
    private String name;
    private Double price;
    private int quantity;
    private Integer id_category;
    private int selectedQuantity;
    private Status status;

    public Product(Integer id_product, String name, Double price, int quantity, Integer id_category, Status status) {
        this.id_product = id_product;
        this.name = name;
        this.price = price;
        this.quantity = quantity;
        this.id_category = id_category;
        this.status = status;
    }

    @Override
    public String toString() {
        return
                "id: " + id_product +
                        " | name: " + name +
                        " | price: " + price + "zl" +
                        " | quantity: " + quantity +
                        " | " + status +
                        "\n---------------------------------------------------";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Product product)) return false;
        return quantity == product.quantity && selectedQuantity == product.selectedQuantity && Objects.equals(id_product, product.id_product) && Objects.equals(name, product.name) && Objects.equals(price, product.price) && Objects.equals(id_category, product.id_category) && status == product.status;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id_product, name, price, quantity, id_category, selectedQuantity, status);
    }
}




