package edu.byui.apj.storefront.db.service;

import edu.byui.apj.storefront.db.model.Cart;
import edu.byui.apj.storefront.db.model.Item;
import edu.byui.apj.storefront.db.repository.CartRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Slf4j
@Service
public class CartService {
    @Autowired
    private CartRepository cartRepository;

    public Cart addItemToCart(String cartId, Item item) {
        // Find the cart
        Cart cart = cartRepository.findById(cartId)
                .orElseThrow(() -> new RuntimeException("Cart not found"));

        // Set the item's cart reference
        item.setCart(cart);

        // Add item to cart's list
        cart.getItems().add(item);

        // Save the updated cart (items will also be saved due to cascade)
        return cartRepository.save(cart);
    }

    public Cart removeItemFromCart(String cartId, Long itemId) {
        Cart cart = cartRepository.findById(cartId)
                .orElseThrow(() -> new RuntimeException("Cart not found"));

        // Remove the item from the cart's list
        cart.getItems().removeIf(item -> item.getId().equals(itemId));

        // Save updated cart
        return cartRepository.save(cart);
    }

    public Cart updateCartItem(String cartId, Item item) {
        // Find the cart
        Cart cart = cartRepository.findById(cartId)
                .orElseThrow(() -> new RuntimeException("Cart not found"));
        log.info("Updating item {}", item);

        // Ensure item exists in the cart, then update
        cart.getItems().removeIf(i -> i.getId().equals(item.getId()));
        item.setCart(cart);
        cart.getItems().add(item);

        // Save the updated cart
        return cartRepository.save(cart);
    }

    public Cart getCart(String cartId) {
        return cartRepository.findById(cartId)
                .orElseThrow(() -> new RuntimeException("Cart not found"));
    }

    public Cart saveCart(Cart cart){
        return cartRepository.save(cart);
    }
}

