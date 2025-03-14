package edu.byui.apj.storefront.apimongo.controller;

import edu.byui.apj.storefront.apimongo.model.TradingCard;
import edu.byui.apj.storefront.apimongo.repository.TradingCardRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/trading-cards")
public class TradingCardController {

    private final TradingCardRepository tradingCardRepository;

    public TradingCardController(TradingCardRepository tradingCardRepository) {
        this.tradingCardRepository = tradingCardRepository;
    }

    // ✅ Get all trading cards (sorted and paginated)
    @GetMapping
    public ResponseEntity<List<TradingCard>> getAllTradingCards(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "8") int size,
            @RequestParam(required = false) String sort) {
        Pageable pageable;
        if (sort == null || sort.isBlank()){
            pageable = PageRequest.of(page, size);
        } else {
            Sort mysort = Sort.by(sort);
            pageable = PageRequest.of(page, size, mysort);
        }
        return ResponseEntity.ok(tradingCardRepository.findAll(pageable).getContent());
    }

    // ✅ Get a trading card by ID
    @GetMapping("/{id}")
    public ResponseEntity<TradingCard> getTradingCardById(@PathVariable String id) {
        return tradingCardRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // ✅ Create a new trading card
    @PostMapping
    public ResponseEntity<TradingCard> createTradingCard(@RequestBody TradingCard tradingCard) {
        TradingCard savedCard = tradingCardRepository.save(tradingCard);
        return ResponseEntity.ok(savedCard);
    }

    // ✅ Update a trading card
    @PutMapping("/{id}")
    public ResponseEntity<TradingCard> updateTradingCard(@PathVariable String id, @RequestBody TradingCard updatedCard) {
        return tradingCardRepository.findById(id)
                .map(existingCard -> {
                    updatedCard.set_id(id);
                    return ResponseEntity.ok(tradingCardRepository.save(updatedCard));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    // ✅ Delete a trading card
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTradingCard(@PathVariable String id) {
        if (tradingCardRepository.existsById(id)) {
            tradingCardRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    // ✅ Filter by specialty
    @GetMapping("/filter/specialty")
    public ResponseEntity<List<TradingCard>> filterBySpecialty(
            @RequestParam String specialty) {
        return ResponseEntity.ok(tradingCardRepository.findBySpecialty(specialty));
    }

    // ✅ Filter by price range
    @GetMapping("/filter/price")
    public ResponseEntity<List<TradingCard>> filterByPriceRange(
            @RequestParam Double minPrice,
            @RequestParam Double maxPrice) {

        return ResponseEntity.ok(tradingCardRepository.findByPriceBetween(minPrice, maxPrice));
    }

    // ✅ Search trading cards by name or contribution
    @GetMapping("/search")
    public ResponseEntity<List<TradingCard>> searchTradingCards(
            @RequestParam String query) {

        return ResponseEntity.ok(tradingCardRepository.findByNameContainsIgnoreCaseOrContributionContainsIgnoreCase(query, query));
    }
}
