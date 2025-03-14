package edu.byui.apj.storefront.apimongo.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class TradingCard {
    private String _id;
    private String name;
    private String specialty;
    private String contribution;
    private Double price;
    private String imageUrl;
}