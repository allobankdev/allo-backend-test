package cory.sakti.Financial.service;

import com.fasterxml.jackson.databind.JsonNode;
import cory.sakti.Financial.strategy.AbstractFinancialStrategy;
import org.springframework.stereotype.Service;


import java.util.HashMap;
import java.util.Map;

@Service("supported_currencies")
public class SupportedCurrenciesService extends AbstractFinancialStrategy {
    @Override
    public String getResourceType() { return "supported_currencies"; }

    @Override
    protected String getUri() { return "/currencies"; }

    @Override
    protected Object transform(JsonNode node) {
        Map<String, String> currencies = new HashMap<>();

        node.fields().forEachRemaining(entry -> {
            currencies.put(entry.getKey(), entry.getValue().asText());
        });

        return Map.copyOf(currencies);
    }
}