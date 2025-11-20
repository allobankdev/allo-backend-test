package com.allobank.service;

import com.allobank.dto.CurrenciesResponse;
import com.allobank.dto.HistoricalRatesResponse;
import com.allobank.dto.LatestRatesResponse;
import com.allobank.model.Cache;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Map;

@Service
public class FinanceService {

    private final Cache cache;

    public FinanceService(Cache cache) {
        this.cache = cache;
    }

    @SuppressWarnings("unchecked")
    public Object getData(String type) {
        return switch (type.toLowerCase()) {
            case "latest" -> {
                Map<String, Object> raw = (Map<String, Object>) cache.get("latest");
                Map<String, Double> rates = (Map<String, Double>) raw.get("rates");
                yield new LatestRatesResponse("IDR", LocalDate.now(), rates);
            }
            case "historical" -> {
                Map<String, Object> raw = (Map<String, Object>) cache.get("historical");
                Map<String, Map<String, Double>> ratesByDate = (Map<String, Map<String, Double>>) raw.get("rates");
                yield new HistoricalRatesResponse(ratesByDate);  // Pass the sub-map directly
            }
            case "currencies" -> {
                Map<String, String> currencies = (Map<String, String>) cache.get("currencies");
                yield new CurrenciesResponse(currencies);
            }
            default -> throw new IllegalArgumentException(
                    "Invalid resourceType. Supported: latest, historical, currencies");
        };
    }
}