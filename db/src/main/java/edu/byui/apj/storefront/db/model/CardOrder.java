package edu.byui.apj.storefront.db.model;

import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;

@Entity
@Data
public class CardOrder {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @OneToOne(cascade = CascadeType.ALL)
    Cart cart;

    @OneToOne(cascade = CascadeType.ALL)
    Address shippingAddress;

    @OneToOne(cascade = CascadeType.ALL)
    Customer customer;

    Date orderDate;
    boolean confirmationSent;
    String shipMethod;
    String orderNotes;
    Double subtotal;
    Double total;
    Double tax;

}
