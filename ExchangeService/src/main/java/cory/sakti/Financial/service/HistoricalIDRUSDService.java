package cory.sakti.Financial.service;

import com.fasterxml.jackson.databind.JsonNode;
import cory.sakti.Financial.strategy.AbstractFinancialStrategy;
import org.springframework.stereotype.Service;


import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

@Service("historical_idr_usd")
public class HistoricalIDRUSDService extends AbstractFinancialStrategy {

    @Override
    public String getResourceType() {
        return "historical_idr_usd";
    }

    @Override
    protected String getUri() {
        // Requirement: Range for IDR to USD
        return "/2024-01-01..2024-01-02?from=IDR&to=USD";
    }

    @Override
    protected Object transform(JsonNode node) {
        Map<String, Map<String, BigDecimal>> outerMap = new HashMap<>();

        JsonNode ratesNode = node.path("rates");

        ratesNode.fields().forEachRemaining(dateEntry -> {
            String date = dateEntry.getKey();
            Map<String, BigDecimal> dailyRates = new HashMap<>();

            dateEntry.getValue().fields().forEachRemaining(currencyEntry -> {
                dailyRates.put(
                        currencyEntry.getKey(),
                        new BigDecimal(currencyEntry.getValue().asText())
                );
            });

            outerMap.put(date, Map.copyOf(dailyRates));
        });

        return Map.copyOf(outerMap);
    }
}
