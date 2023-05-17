package customers;

import lombok.Getter;

@Getter
public class Customer {
    Integer id_customer;
    String name;
    String lastName;
    String email;
    int number;
    String address;
    String password;




    public Customer(Integer id_customer, String name, String lastName, String email, int number, String address,String password) {
        this.id_customer = id_customer;
        this.name = name;
        this.lastName = lastName;
        this.email = email;
        this.number = number;
        this.address = address;
        this.password = password;

    }


    public String toStringForAdministrator() {
        return
                        "id: " + id_customer +
                        " | Name: " + name  +
                        " | Last Name: " + lastName +
                        " | Email: " + email  +
                        " | Number: " + number +
                        " | Address: " + address +
                                "\n------------------------------------------------------------------------------------";
    }
    public String toStringForCustomer() {
        return
                "id: " + id_customer +
                        " | Name: " + name  +
                        " | Last Name: " + lastName +
                        " | Email: " + email  +
                        " | Number: " + number +
                        " | Address: " + address +
                        " | Password: " + password +
                        "\n------------------------------------------------------------------------------------";
    }
}

