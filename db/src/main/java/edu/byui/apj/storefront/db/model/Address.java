package edu.byui.apj.storefront.db.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

@Data
@Entity
public class Address {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    String addressLine1;
    String addressLine2;
    String city;
    String state;
    String zipCode;
    String country;
}
