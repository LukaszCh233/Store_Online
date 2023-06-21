package customers;

import lombok.Getter;
import lombok.Setter;

import java.util.Objects;

@Getter
@Setter
public class Customer {
   private Integer id_customer;
    private String name;
    private String lastName;
    private String email;
    private Integer number;
    private String address;
    private String password;

    public Customer(Integer id_customer, String name, String lastName, String email, Integer number, String address, String password) {
        this.id_customer = id_customer;
        this.name = name;
        this.lastName = lastName;
        this.email = email;
        this.number = number;
        this.address = address;
        this.password = password;

    }

    public String toStringForCustomer() {
        return "id: " + id_customer +
                " | Name: " + name +
                " | Last Name: " + lastName +
                " | Email: " + email +
                " | Number: " + number +
                " | Address: " + address +
                " | Password: " + password +
                "\n------------------------------------------------------------------------------------";
    }
    public String toStringForAdministrator() {
        return "id: " + id_customer +
                " | Name: " + name +
                " | Last Name: " + lastName +
                " | Email: " + email +
                " | Number: " + number +
                " | Address: " + address +
                "\n------------------------------------------------------------------------------------";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Customer customer)) return false;
        return Objects.equals(id_customer, customer.id_customer) && Objects.equals(name, customer.name) && Objects.equals(lastName, customer.lastName) && Objects.equals(email, customer.email) && Objects.equals(number, customer.number) && Objects.equals(address, customer.address) && Objects.equals(password, customer.password);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id_customer, name, lastName, email, number, address, password);
    }
}

