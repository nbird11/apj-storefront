package edu.byui.apj.storefront.api.service;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import edu.byui.apj.storefront.api.model.TradingCard;

@Service
public class TradingCardService {

    private final List<TradingCard> tradingCards = new ArrayList<>();

    public TradingCardService() {
        loadAllFromCsv();
    }

    private void loadAllFromCsv() {
        try {
            ClassPathResource resource = new ClassPathResource("pioneers.csv");
            Reader reader = new InputStreamReader(resource.getInputStream());
            Iterable<CSVRecord> records = CSVFormat.DEFAULT.builder()
                .setHeader()
                .build()
                .parse(reader);

            for (CSVRecord record : records) {
                long id = Integer.parseInt(record.get("ID"));
                String name = record.get("Name");
                String specialty = record.get("Specialty");
                String contribution = record.get("Contribution");
                BigDecimal price = new BigDecimal(record.get("Price"));
                String imgURL = record.get("ImageUrl");
                tradingCards.add(new TradingCard(id, name, specialty, contribution, price, imgURL));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public List<TradingCard> getCards(int page, int size) {
        List<Integer> pair = handlePagination(page, size);
        int start = pair.getFirst();
        int end = pair.getLast();
        return tradingCards.subList(start, end);
    }

    public List<TradingCard> getCardsFilter(
        Optional<BigDecimal> maybeMinPrice,
        Optional<BigDecimal> maybeMaxPrice,
        Optional<String> maybeSpecialty,
        Optional<String> maybeSort
    ) {

        List<TradingCard> filteredCards = new ArrayList<>(tradingCards.stream()
            .filter(card ->
                maybeMinPrice.map(price -> card.getPrice().compareTo(price) >= 0)
                    .orElse(true))
            .filter(card ->
                maybeMaxPrice.map(price -> card.getPrice().compareTo(price) <= 0)
                    .orElse(true))
            .filter(card ->
                maybeSpecialty.map(spec -> clean(card.getSpecialty()).contains(clean(spec)))
                    .orElse(true))
            .toList());

        maybeSort.ifPresent(sort -> {
            if (sort.equals("price")) {
                filteredCards.sort(Comparator.comparing(TradingCard::getPrice));
            } else if (sort.equals("name")) {
                filteredCards.sort(Comparator.comparing(TradingCard::getName));
            } else {
                throw new AssertionError("Invalid sort: " + sort); // Should be caught in controller
            }
        });

        return filteredCards;
    }

    public List<TradingCard> getCardsSearch(String query) {
        return tradingCards.stream()
            .filter(card -> clean(card.getName()).contains(clean(query))
                || clean(card.getContribution()).contains(clean(query)))
            .toList();
    }

    private List<Integer> handlePagination(int page, int size) {
        int maxSize = tradingCards.size();

        int clampedSize = Math.clamp(size, 1, maxSize);
        int clampedPage = Math.clamp(page, 0, (int)Math.ceil((double)(maxSize - clampedSize) / clampedSize));
        // integer cast to double divided by int = double => Take ceiling which rounds up, still double => cast to int.
        // subtracting 1 size because page is zero-indexed

        int start = clampedPage * clampedSize;
        int end = Math.min(clampedPage * clampedSize + clampedSize, tradingCards.size());
        return List.of(start, end);
    }

    private String clean(String str) {
        if (str == null) throw new AssertionError("string was null");
        return str.trim().toLowerCase();
    }
}
