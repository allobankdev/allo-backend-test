package cory.sakti.Financial.service;

import com.fasterxml.jackson.databind.JsonNode;
import cory.sakti.Financial.strategy.AbstractFinancialStrategy;
import org.springframework.stereotype.Service;


import java.util.HashMap;

@Service("supported_currencies")
public class SupportedCurrenciesService extends AbstractFinancialStrategy {
    @Override
    public String getResourceType() { return "supported_currencies"; }

    @Override
    protected String getUri() { return "/currencies"; }

    @Override
    protected Object transform(JsonNode node) {
        // RED: Returning a mutable HashMap to fail the immutability check
        return new HashMap<String, String>();
    }
}