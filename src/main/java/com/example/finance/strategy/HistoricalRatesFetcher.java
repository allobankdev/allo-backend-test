package com.example.finance.strategy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.example.finance.client.FrankfurterClient;
import com.example.finance.dto.HistoricalRatesResponse;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component("historical_idr_usd")
public class HistoricalRatesFetcher implements IDRDataFetcher {

    private static final Logger log = LoggerFactory.getLogger(HistoricalRatesFetcher.class);

    private final FrankfurterClient client;

    public HistoricalRatesFetcher(FrankfurterClient client) {
        this.client = client;
    }

    @Override
    public String getType() {
        return "historical_idr_usd";
    }

    @Override
    public Object fetchData() {
        log.info("Fetching historical IDR → USD rates...");

        try {
            String response = client.getHistoricalRates();

            if (response == null) {
                throw new RuntimeException("Historical API returned null response");
            }

            ObjectMapper mapper = new ObjectMapper();
            HistoricalRatesResponse result =
                    mapper.readValue(response, HistoricalRatesResponse.class);

            if (result.getRates() == null || result.getRates().isEmpty()) {
                throw new RuntimeException("Historical rates data is empty");
            }

            return result;

        } catch (Exception e) {
            log.error("Failed to process historical rates", e);
            throw new RuntimeException("Failed to process data", e);
        }
    }
}