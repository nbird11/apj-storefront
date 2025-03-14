package edu.byui.apj.storefront.web.service;
import edu.byui.apj.storefront.web.model.MongoTradingCard;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
public class MongoTradingCardClientService {

    private final WebClient webClient;

    public MongoTradingCardClientService(@Value("${tradingcard.mongo.service.url}/api/trading-cards") String tradingCardServiceUrl) {
        this.webClient = WebClient.builder().baseUrl(tradingCardServiceUrl).build();
    }

    // ✅ Get all trading cards (paginated & sorted)
    public List<MongoTradingCard> getAllTradingCards(int page, int size, String sort) {
        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .queryParam("page", page)
                        .queryParam("size", size)
                        .queryParamIfPresent("sort", Optional.ofNullable(sort))
                        .build())
                .retrieve()
                .bodyToFlux(MongoTradingCard.class)
                .collectList()
                .block();  // Blocking call
    }

    // ✅ Get a single trading card by ID
    public MongoTradingCard getTradingCardById(String id) {
        return webClient.get()
                .uri("/{id}", id)
                .retrieve()
                .bodyToMono(MongoTradingCard.class)
                .block();  // Blocking call
    }

    // ✅ Create a new trading card
    public MongoTradingCard createTradingCard(MongoTradingCard tradingCard) {
        return webClient.post()
                .bodyValue(tradingCard)
                .retrieve()
                .bodyToMono(MongoTradingCard.class)
                .block();  // Blocking call
    }

    // ✅ Update an existing trading card
    public MongoTradingCard updateTradingCard(String id, MongoTradingCard updatedCard) {
        return webClient.put()
                .uri("/{id}", id)
                .bodyValue(updatedCard)
                .retrieve()
                .bodyToMono(MongoTradingCard.class)
                .block();  // Blocking call
    }

    // ✅ Delete a trading card
    public void deleteTradingCard(String id) {
        webClient.delete()
                .uri("/{id}", id)
                .retrieve()
                .bodyToMono(Void.class)
                .block();  // Blocking call
    }

    // ✅ Filter by specialty
    public List<MongoTradingCard> filterBySpecialty(String specialty) {
        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/filter/specialty")
                        .queryParam("specialty", specialty)
                        .build())
                .retrieve()
                .bodyToFlux(MongoTradingCard.class)
                .collectList()
                .block();  // Blocking call
    }

    // ✅ Filter by price range
    public List<MongoTradingCard> filterByPriceRange(BigDecimal minPrice, BigDecimal maxPrice) {
        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/filter/price")
                        .queryParam("minPrice", minPrice)
                        .queryParam("maxPrice", maxPrice)
                        .build())
                .retrieve()
                .bodyToFlux(MongoTradingCard.class)
                .collectList()
                .block();  // Blocking call
    }

    // ✅ Search trading cards by name or contribution
    public List<MongoTradingCard> searchTradingCards(String query) {
        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/search")
                        .queryParam("query", query)
                        .build())
                .retrieve()
                .bodyToFlux(MongoTradingCard.class)
                .collectList()
                .block();  // Blocking call
    }
}
