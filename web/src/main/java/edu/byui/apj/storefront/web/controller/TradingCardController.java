package edu.byui.apj.storefront.web.controller;

import edu.byui.apj.storefront.web.model.TradingCard;
import edu.byui.apj.storefront.web.service.TradingCardClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api")
public class TradingCardController {
    TradingCardClientService tcService;

    @Autowired
    public TradingCardController(TradingCardClientService tcService) {
        this.tcService = tcService;
    }

    @GetMapping("/cards")
    public List<TradingCard> getCards(
        @RequestParam(name = "page", defaultValue = "0") int page,
        @RequestParam(name = "size", defaultValue = "20") int size
    ) {
        return tcService.getAllCardsPaginated(page, size);
    }

    @GetMapping("/cards/filter")
    public ResponseEntity<List<TradingCard>> getCardsFilter(
        @RequestParam(name = "minPrice", required = false) BigDecimal maybeMinPrice,
        @RequestParam(name = "maxPrice", required = false) BigDecimal maybeMaxPrice,
        @RequestParam(name = "specialty", required = false) String maybeSpecialty,
        @RequestParam(name = "sort", required = false) String maybeSort
    ) {
        return ResponseEntity.ok(tcService.filterAndSort(maybeMinPrice, maybeMaxPrice, maybeSpecialty, maybeSort));
    }

    @GetMapping("/cards/search")
    public List<TradingCard> getCardsSearch(
        @RequestParam(name = "query") String query
    ) {
        return tcService.searchByNameOrContribution(query);
    }
}
