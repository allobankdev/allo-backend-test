package com.example.finance.strategy;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.example.finance.client.FrankfurterClient;
import com.example.finance.dto.CurrencyResponse;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component("supported_currencies")
public class SupportedCurrenciesFetcher implements IDRDataFetcher {

    private static final Logger log = LoggerFactory.getLogger(SupportedCurrenciesFetcher.class);

    private final FrankfurterClient client;

    public SupportedCurrenciesFetcher(FrankfurterClient client) {
        this.client = client;
    }

    @Override
    public String getType() {
        return "supported_currencies";
    }

    @Override
    public Object fetchData() {
        log.info("Fetching supported currencies...");

        try {
            String response = client.getCurrencies();

            if (response == null) {
                throw new RuntimeException("Currencies API returned null response");
            }

            ObjectMapper mapper = new ObjectMapper();

            Map<String, String> currencies = mapper.readValue(
                    response,
                    new TypeReference<Map<String, String>>() {}
            );

            if (currencies == null || currencies.isEmpty()) {
                throw new RuntimeException("Currencies data is empty");
            }

            return new CurrencyResponse(currencies);

        } catch (Exception e) {
            log.error("Failed to process supported currencies", e);
            throw new RuntimeException("Failed to process data", e);
        }
    }
}