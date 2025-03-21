package edu.byui.apj.storefront.db.repository;

import edu.byui.apj.storefront.db.model.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;


public interface CartRepository extends JpaRepository<Cart, String> {

    @Query("SELECT c FROM Cart c WHERE c.id NOT IN (SELECT o.cart.id FROM CardOrder o WHERE o.cart IS NOT NULL)")
    List<Cart> findCartsWithoutOrders();
}
