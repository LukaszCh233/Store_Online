package customers;

public class Customer {
    Integer id_customer;
    String name;
    String lastName;
    String email;
    int number;
    String address;



    public Customer(Integer id_customer, String name, String lastName, String email, int number, String address) {
        this.id_customer = id_customer;
        this.name = name;
        this.lastName = lastName;
        this.email = email;
        this.number = number;
        this.address = address;

    }

    @Override
    public String toString() {
        return
                        "id: " + id_customer +
                        " | Name: " + name  +
                        " | Last Name: " + lastName +
                        " | Email: " + email  +
                        " | Number: " + number +
                        " | Address: " + address +
                                "\n------------------------------------------------------------------------------------";
    }
}

