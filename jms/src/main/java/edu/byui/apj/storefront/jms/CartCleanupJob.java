package edu.byui.apj.storefront.jms;

import edu.byui.apj.storefront.model.Cart;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatusCode;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Component
@Slf4j
public class CartCleanupJob {

    private final WebClient webClient;

    public CartCleanupJob(@Value("${db.service.url}") String dbServiceUrl) {
        this.webClient = WebClient.create(dbServiceUrl);
    }

    public List<Cart> getCartsWithoutOrders() {
        try {
            List<Cart> noOrderCarts = webClient.get()
                .uri("/cart/noorder")
                .retrieve()
                .onStatus(HttpStatusCode::isError, ClientResponse::createException)
                .bodyToFlux(Cart.class)
                .collectList()
                .block();
            log.info("Carts without orders: {}", noOrderCarts);
            return noOrderCarts;
        } catch (WebClientResponseException e) {
            log.error("HTTP error: {} - {}", e.getStatusCode(), e.getResponseBodyAsString());
            throw new RuntimeException("Failed to fetch order details: " + e.getStatusText(), e);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new RuntimeException(e);
        }
    }

    public void cleanupCart(String cartId) {
        try {
            webClient.delete()
                .uri("/cart/{cartId}", cartId)
                .retrieve()
                .onStatus(HttpStatusCode::isError, ClientResponse::createException)
                .bodyToFlux(Cart.class)
                .collectList()
                .block();
            log.info("Successfully cleaned up cart: {}", cartId);
        } catch (WebClientResponseException e) {
            log.error("HTTP error while deleting cart {}: {} - {}", cartId, e.getStatusCode(), e.getResponseBodyAsString());
            throw new RuntimeException("Failed to delete cart: " + e.getStatusText(), e);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new RuntimeException(e);
        }
    }

    @Scheduled(cron = "0 0 3 * * *")
    public void cleanupCarts() {
        List<Cart> noOrderCarts = getCartsWithoutOrders();
        ExecutorService executor = Executors.newFixedThreadPool(2);

        // batch 1: first half of list of empty carts
        executor.submit(() ->
            noOrderCarts.subList(0, noOrderCarts.size() / 2).stream()
                .map(Cart::getId).forEach(this::cleanupCart)
        );
        // batch 2: second half of list of empty carts
        executor.submit(() ->
            noOrderCarts.subList(noOrderCarts.size() / 2, noOrderCarts.size()).stream()
                .map(Cart::getId).forEach(this::cleanupCart));

        log.info("Cart cleanup complete.");
    }

}
