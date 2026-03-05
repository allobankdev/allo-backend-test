package com.allobank.strategy;

import com.allobank.config.FrankfurterClientFactory;
import com.allobank.services.SpreadCalculator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.Map;

@Component("latest_idr_rates")  
@Slf4j
public class LatestIdrRatesFetcher implements IDRDataFetcher {

    private final RestTemplate restTemplate;
    private final String baseUrl;
    private final com.allobank.services.SpreadCalculator spreadCalculator;

    public LatestIdrRatesFetcher(FrankfurterClientFactory clientFactory, 
                                 SpreadCalculator spreadCalculator) {
        this.restTemplate = clientFactory.getObject();
        this.baseUrl = clientFactory.getBaseUrl();
        this.spreadCalculator = spreadCalculator;
    }

    @Override
    public Object fetchData() {
        log.info("Fetching latest IDR rates...");
        
     
        String url = baseUrl + "/latest?base=IDR";
        Map response = restTemplate.getForObject(url, Map.class);
        
        if (response == null) {
            throw new RuntimeException("Failed to fetch latest rates");
        }
        
       
        return transformWithSpread(response);
    }

    private Map<String, Object> transformWithSpread(Map response) {
        Map<String, Object> result = new HashMap<>();
        result.put("base", response.get("base"));
        result.put("date", response.get("date"));
        
        Map<String, Object> rates = (Map<String, Object>) response.get("rates");
        Map<String, Object> newRates = new HashMap<>(rates);
        
        // Hitung spread khusus untuk USD
        if (rates.containsKey("USD")) {
            BigDecimal rateUsd = new BigDecimal(rates.get("USD").toString());
            BigDecimal spreadRate = calculateBuySpread(rateUsd);
            newRates.put("USD_BuySpread_IDR", spreadRate);
            log.debug("USD_BuySpread_IDR calculated: {}", spreadRate);
        }
        
        result.put("rates", newRates);
        return result;
    }

    private BigDecimal calculateBuySpread(BigDecimal rateUsd) {
        BigDecimal idrPerUsd = BigDecimal.ONE.divide(rateUsd, 10, RoundingMode.HALF_UP);
        
        double spreadFactor = spreadCalculator.calculate();
        BigDecimal multiplier = BigDecimal.ONE.add(BigDecimal.valueOf(spreadFactor));
        
        return idrPerUsd.multiply(multiplier).setScale(2, RoundingMode.HALF_UP);
    }

    @Override
    public String getResourceType() {
        return "latest_idr_rates";
    }
}