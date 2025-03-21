package edu.byui.apj.storefront.model;

import lombok.Data;

import java.util.List;

@Data
public class Cart {

    String id;
    String personId;
    List<Item> items;
    
}
