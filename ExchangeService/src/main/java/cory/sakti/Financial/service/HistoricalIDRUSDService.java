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
        // RED PHASE: Returning a standard mutable nested HashMap.
        // This will fail the "Deep Immutability" assertion in the test.
        return new HashMap<String, Map<String, BigDecimal>>();
    }
}
