package edu.byui.apj.storefront.jms.producer;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jms.JmsException;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class OrderConfirmationProducer {

    private final JmsTemplate jmsTemplate;

    public OrderConfirmationProducer(JmsTemplate jmsTemplate) {
        this.jmsTemplate = jmsTemplate;
    }

    public void confirmOrder(String orderId) throws RuntimeException {
        log.info("Trying to send order confirmation...");
        try {
            jmsTemplate.convertAndSend("orderQueue", orderId);
        } catch (JmsException e) {
            log.error(e.getMessage(), e);
            throw new RuntimeException(e);
        }
        log.info("Sent order confirmation request for orderId {} with no errors.", orderId);
    }
}
