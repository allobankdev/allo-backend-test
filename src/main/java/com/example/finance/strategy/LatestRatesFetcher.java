package com.example.finance.strategy;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.example.finance.client.FrankfurterClient;
import com.example.finance.dto.LatestRatesResponse;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component("latest_idr_rates")
public class LatestRatesFetcher implements IDRDataFetcher {
	private static final Logger log = LoggerFactory.getLogger(LatestRatesFetcher.class);

    private final FrankfurterClient client;

    public LatestRatesFetcher(FrankfurterClient client) {
        this.client = client;
    }

    @Override
    public String getType() {
        return "latest_idr_rates";
    }

    @Override
    public Object fetchData() {
    		log.info("Fetching latest IDR rates...");

        try {
            String response = client.getLatestRates();

            ObjectMapper mapper = new ObjectMapper();
            Map<String, Object> json = mapper.readValue(response, Map.class);

            Map<String, Double> rates = mapper.convertValue(
            	    json.get("rates"),
            	    new com.fasterxml.jackson.core.type.TypeReference<Map<String, Double>>() {}
            	);

            Double usdRate = rates.get("USD");
            
            if (usdRate == null || usdRate == 0) {
                throw new RuntimeException("USD rate not available");
            }

            double spreadFactor = calculateSpreadFactor();

            double result = (1 / usdRate) * (1 + spreadFactor);

            return new LatestRatesResponse(result, rates);

        } catch (Exception e) {
            log.error("Failed to process latest rates", e);
            throw new RuntimeException("Failed to process data", e);
        }
    }
    
    private double calculateSpreadFactor() {
        String username = "fauziladzuardhirokhmana";

        int sum = 0;
        for (char c : username.toCharArray()) {
            sum += (int) c;
        }

        return (sum % 1000) / 100000.0;
    }
}