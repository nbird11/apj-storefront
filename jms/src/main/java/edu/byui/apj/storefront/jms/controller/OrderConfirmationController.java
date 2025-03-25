package edu.byui.apj.storefront.jms.controller;

import edu.byui.apj.storefront.jms.producer.OrderConfirmationProducer;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/confirm")
public class OrderConfirmationController {

    private final OrderConfirmationProducer orderConfirmationProducer;

    public OrderConfirmationController(OrderConfirmationProducer orderConfirmationProducer) {
        this.orderConfirmationProducer = orderConfirmationProducer;
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<String> confirmOrder(@PathVariable String orderId) {
            orderConfirmationProducer.confirmOrder(orderId);
        return ResponseEntity.ok().body("Order confirm message sent for order ID: " + orderId);
    }

}
