package edu.byui.apj.storefront.db.controller;

import edu.byui.apj.storefront.db.model.Cart;
import edu.byui.apj.storefront.db.model.Item;
import edu.byui.apj.storefront.db.service.CartService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/cart")
@CrossOrigin(origins = {"http://localhost:8080","http://localhost:8083"})
public class CartController {

    private final CartService cartService;

    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    @GetMapping("/noorder")
    public ResponseEntity<List<Cart>> getCartNoOrder() {
        List<Cart> mycarts = cartService.getCartsWithoutOrders();
        return ResponseEntity.ok(mycarts);
    }

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

    @DeleteMapping("/{cartId}")
    public void removeCart(@PathVariable String cartId) {
        cartService.removeCart(cartId);
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

