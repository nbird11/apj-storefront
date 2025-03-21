package edu.byui.apj.storefront.model;

import lombok.Data;

@Data
public class Address {

    Long id;
    String addressLine1;
    String addressLine2;
    String city;
    String state;
    String zipCode;
    String country;
    
}
