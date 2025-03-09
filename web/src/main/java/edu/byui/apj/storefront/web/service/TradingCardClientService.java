package edu.byui.apj.storefront.web.service;

import edu.byui.apj.storefront.web.model.TradingCard;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.math.BigDecimal;
import java.util.List;

@Service
public class TradingCardClientService {
    private final WebClient webClient;

    public TradingCardClientService(@Value("${trading-card.service.url}") String tcBaseUrl) {
        this.webClient = WebClient.builder().baseUrl(tcBaseUrl).build();
    }

    // Returns the list of cards starting at position page * size and returning size elements.
    public List<TradingCard> getAllCardsPaginated(int page, int size) {
        return webClient.get()
            .uri(uriBuilder -> uriBuilder
                .path("/api/cards")
                .queryParam("page", page)
                .queryParam("size", size)
                .build())
            .retrieve()
            .bodyToFlux(TradingCard.class)
            .collectList()
            .block();
    }

    // Returns the list resulting in filtering by minPrice, maxPrice or specialty, then sorting by sort.
    // Sort can be "name" or "price"
    public List<TradingCard> filterAndSort(BigDecimal minPrice, BigDecimal maxPrice, String specialty, String sort) {
        return webClient.get()
            .uri(uriBuilder -> uriBuilder
                .path("/api/cards/filter")
                .queryParam("minPrice", minPrice)
                .queryParam("maxPrice", maxPrice)
                .queryParam("specialty", specialty)
                .queryParam("sort", sort)
                .build()
            )
            .retrieve()
            .bodyToFlux(TradingCard.class)
            .collectList()
            .block();
    }

    // Returns the list of cards resulting in the query string (case insensitive) found in the name or contribution.
    public List<TradingCard> searchByNameOrContribution(String query) {
        return webClient.get()
            .uri(uriBuilder -> uriBuilder
                .path("/api/cards/search")
                .queryParam("query", query)
                .build()
            )
            .retrieve()
            .bodyToFlux(TradingCard.class)
            .collectList()
            .block();
    }
}
