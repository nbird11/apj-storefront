package edu.byui.apj.storefront.jms.consumer;

import edu.byui.apj.storefront.model.CardOrder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatusCode;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

@Component
@Slf4j
public class OrderConfirmationConsumer {

    @JmsListener(destination = "orderQueue")
    public void receiveOrderConfirmation(@Value("${db.service.url}") String dbServiceUrl, String orderId) {
        log.info("Received order confirmation request for orderId {} ...", orderId);
        try {
            CardOrder order = WebClient.create(dbServiceUrl).get()
                .uri("/order/{orderId}", orderId)
                .retrieve()
                .onStatus(HttpStatusCode::isError, ClientResponse::createException)
                .bodyToMono(CardOrder.class)
                .block();
            log.info("Card order confirmation received for {}: {}", orderId, order);
        } catch (WebClientResponseException e) {
            log.error("HTTP error for order {}: {} - {}", orderId, e.getStatusCode(), e.getResponseBodyAsString());
            throw new RuntimeException("Failed to fetch order details: " + e.getStatusText(), e);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new RuntimeException(e);
        }
    }

}
