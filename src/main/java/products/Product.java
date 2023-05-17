package products;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Product {
    Integer id_product;
    String name;
    Double price;
    int quantity;
    Integer id_category;
    int selectedQuantity;



    public Product(Integer id_product, String name, Double price, int quantity, Integer id_category) {
        this.id_product = id_product;
        this.name = name;
        this.price = price;
        this.quantity = quantity;
        this.id_category = id_category;

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


