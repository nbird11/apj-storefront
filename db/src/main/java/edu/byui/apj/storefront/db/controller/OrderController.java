package edu.byui.apj.storefront.db.controller;

import edu.byui.apj.storefront.db.model.CardOrder;
import edu.byui.apj.storefront.db.service.OrderService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/order")
@CrossOrigin(origins = "http://localhost:8080")
public class OrderController {

    OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping
    public ResponseEntity<CardOrder> saveOrder(@RequestBody CardOrder order) {
        CardOrder savedOrder = orderService.saveOrder(order);
        return ResponseEntity.ok(savedOrder);
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<CardOrder> getOrder(@PathVariable("orderId") Long orderId) {
        Optional<CardOrder> order = orderService.getOrder(orderId);
        return order.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

}
