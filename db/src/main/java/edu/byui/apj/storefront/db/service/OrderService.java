package edu.byui.apj.storefront.db.service;

import edu.byui.apj.storefront.db.model.CardOrder;
import edu.byui.apj.storefront.db.model.Cart;
import edu.byui.apj.storefront.db.repository.OrderRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class OrderService {
    OrderRepository orderRepository;
    CartService cartService;

    public OrderService(OrderRepository orderRepository, CartService cartService) {
        this.orderRepository = orderRepository;
        this.cartService = cartService;
    }

    public CardOrder saveOrder(CardOrder order) {
        Cart cart = cartService.getCart(order.getCart().getId());
        order.setCart(cart);
        return orderRepository.save(order);
    }

    public Optional<CardOrder> getOrder(Long orderid) {
        return orderRepository.findById(orderid);
    }
}
