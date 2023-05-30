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
    Status status;


    public Order(Integer idOrder, Integer idCustomer, LocalDate orderData, Double price, Status status) {
        this.idOrder = idOrder;
        this.idCustomer = idCustomer;
        this.orderData = orderData;
        this.price = price;
        this.status =status;

    }


    public String toStringForAdministrator() {
        return
                "Order id: " + idOrder +
                " | Customer id: " + idCustomer +
                " | Order data: " + orderData +
                " | Cost: " + price +"zl" +
                        " | " + status;
    }
    public String toStringForCustomer() {
        return
                        " | Customer id: " + idCustomer +
                        " | Order data: " + orderData +
                        " | Cost: " + price +"zl" +
                                " | " + status;
    }
}
