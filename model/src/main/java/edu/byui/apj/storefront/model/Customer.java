package edu.byui.apj.storefront.model;

import lombok.Data;

@Data
public class Customer {

    Long id;
    String firstName;
    String lastName;
    String email;
    String phone;

}
