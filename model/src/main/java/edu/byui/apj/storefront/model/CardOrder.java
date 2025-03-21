package edu.byui.apj.storefront.model;

import lombok.Data;

import java.util.Date;

@Data
public class CardOrder {

    Long id;
    Cart cart;
    Address shippingAddress;
    Customer customer;
    Date orderDate;
    boolean confirmationSent;
    String shipMethod;
    String orderNotes;
    Double subtotal;
    Double total;
    Double tax;

}
