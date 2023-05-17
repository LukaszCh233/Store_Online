package products;

import lombok.Getter;
import lombok.Setter;
import java.time.LocalDate;

@Getter
@Setter
public class Order {
    Integer idOrder;
    Integer idCustomer;
    LocalDate orderData;
    Double price;

    public Order(Integer idOrder, Integer idCustomer, LocalDate orderData, Double price) {
        this.idOrder = idOrder;
        this.idCustomer = idCustomer;
        this.orderData = orderData;
        this.price = price;
    }

    @Override
    public String toString() {
        return
                "Order id: " + idOrder +
                " | Customer id: " + idCustomer +
                " | Order data: " + orderData +
                " | Cost: " + price +"zl";
    }
}
