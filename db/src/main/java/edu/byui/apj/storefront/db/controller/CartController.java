package edu.byui.apj.storefront.db.controller;

import edu.byui.apj.storefront.db.model.Cart;
import edu.byui.apj.storefront.db.model.Item;
import edu.byui.apj.storefront.db.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/cart")
@CrossOrigin(origins = "*")
public class CartController {

    @Autowired
    private CartService cartService;

    @GetMapping("/{cartId}")
    public ResponseEntity<Cart> getCart(@PathVariable String cartId) {
        Cart mycart = cartService.getCart(cartId);
        return ResponseEntity.ok(mycart);
    }

    @PostMapping
    public ResponseEntity<Cart> saveCart(@RequestBody Cart cart) {
        Cart savedCart = cartService.saveCart(cart);
        return ResponseEntity.ok(savedCart);
    }

    @PostMapping("/{cartId}/item")
    public ResponseEntity<Cart> addItemToCart(@PathVariable String cartId, @RequestBody Item item) {
        Cart updatedCart = cartService.addItemToCart(cartId, item);
        return ResponseEntity.ok(updatedCart);
    }

    @DeleteMapping("/{cartId}/item/{itemId}")
    public ResponseEntity<Cart> removeItemFromCart(@PathVariable String cartId, @PathVariable Long itemId) {
        Cart updatedCart = cartService.removeItemFromCart(cartId, itemId);
        return ResponseEntity.ok(updatedCart);
    }

    @PutMapping("/{cartId}/item")
    public ResponseEntity<Cart> updateItemInCart(@PathVariable String cartId, @RequestBody Item item) {
        Cart updatedCart = cartService.updateCartItem(cartId, item);
        return ResponseEntity.ok(updatedCart);
    }

}

