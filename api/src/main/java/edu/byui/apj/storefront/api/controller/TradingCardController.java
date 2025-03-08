package edu.byui.apj.storefront.api.controller;

import edu.byui.apj.storefront.api.model.TradingCard;
import edu.byui.apj.storefront.api.service.TradingCardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api")
public class TradingCardController {
    TradingCardService tcService;

    @Autowired
    public TradingCardController(TradingCardService tcService) {
        this.tcService = tcService;
    }

    @GetMapping("/cards")
    public List<TradingCard> getCards(
        @RequestParam(name = "page", defaultValue = "0") int page,
        @RequestParam(name = "size", defaultValue = "20") int size
    ) {
        return tcService.getCards(page, size);
    }

    @GetMapping("/cards/filter")
    public ResponseEntity<List<TradingCard>> getCardsFilter(
        @RequestParam(name = "minPrice", required = false) Optional<BigDecimal> maybeMinPrice,
        @RequestParam(name = "maxPrice", required = false) Optional<BigDecimal> maybeMaxPrice,
        @RequestParam(name = "specialty", required = false) Optional<String> maybeSpecialty,
        @RequestParam(name = "sort", required = false) Optional<String> maybeSort
    ) {
        if (maybeSort.isPresent()) {
            if (!maybeSort.get().isBlank()) {
                String sort = maybeSort.get();

                if (!sort.equals("name") && !sort.equals("price")) {
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "sort must be either 'name' or 'price'");
                }
            }
            else {
                maybeSort = Optional.empty();
            }
        }
        return ResponseEntity.ok(tcService.getCardsFilter(maybeMinPrice, maybeMaxPrice, maybeSpecialty, maybeSort));
    }

    @GetMapping("/cards/search")
    public List<TradingCard> getCardsSearch(
        @RequestParam(name = "query") String query
    ) {
        return tcService.getCardsSearch(query);
    }
}
