package edu.byui.apj.storefront.db.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Entity
@Data
public class Item {

    @Id
    @EqualsAndHashCode.Include
    Long id;

    @JsonBackReference
    @ManyToOne
    @JoinColumn(name = "cart_id")
    Cart cart;

    String cardId;
    String name;
    Double price;
    Integer quantity;
}
