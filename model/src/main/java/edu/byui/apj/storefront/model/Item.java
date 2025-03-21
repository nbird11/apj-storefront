package edu.byui.apj.storefront.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
public class Item {

    @EqualsAndHashCode.Include
    Long id;
    Cart cart;
    String cardId;
    String name;
    Double price;
    Integer quantity;
    
}
