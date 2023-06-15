package products;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.Objects;

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
        this.status = status;

    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Order order)) return false;
        return Objects.equals(idOrder, order.idOrder) && Objects.equals(idCustomer, order.idCustomer) && Objects.equals(orderData, order.orderData) && Objects.equals(price, order.price) && status == order.status;
    }

    @Override
    public int hashCode() {
        return Objects.hash(idOrder, idCustomer, orderData, price, status);
    }

    public String toStringForAdministrator() {
        return
                "Order id: " + idOrder +
                        " | Customer id: " + idCustomer +
                        " | Order data: " + orderData +
                        " | Cost: " + price + "zl" +
                        " | " + status;
    }

    public String toStringForCustomer() {
        return
                " | Customer id: " + idCustomer +
                        " | Order data: " + orderData +
                        " | Cost: " + price + "zl" +
                        " | " + status;
    }
}
