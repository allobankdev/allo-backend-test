package com.example.assesment_be_allo.service;
import com.example.assesment_be_allo.dto.LatestRatesResponse;
import com.example.assesment_be_allo.repository.ExternalApiRepository;
import org.springframework.stereotype.Service;
import java.util.*;

@Service
public class LatestIdrRatesService {

    private final ExternalApiRepository repository;
    private final SpreadCalculatorService spreadCalculatorService;

    public LatestIdrRatesService(ExternalApiRepository repository,
                                 SpreadCalculatorService spreadCalculatorService) {
        this.repository = repository;
        this.spreadCalculatorService = spreadCalculatorService;
    }

    public Object fetchLatestRates() {
        Map<String, Object> apiResponse = repository.fetchLatestRates("IDR");

        if (apiResponse == null) {
            return Collections.emptyList();
        }

        // Calculate unique spread factor
        double spreadFactor = spreadCalculatorService.calculateSpreadFactor();

        // Extract rates
        Map<String, Object> rates = (Map<String, Object>) apiResponse.get("rates");

        // Calculate USD_BuySpread_IDR
        Double usdRate = extractUsdRate(rates);
        Double usdBuySpreadIdr = spreadCalculatorService.calculateUsdBuySpreadIdr(
                usdRate, spreadFactor);

        // Build response DTO
        LatestRatesResponse response = new LatestRatesResponse();
        response.setBase((String) apiResponse.get("base"));
        response.setDate((String) apiResponse.get("date"));
        response.setRates(rates);
        response.setUsdBuySpreadIdr(usdBuySpreadIdr);
        response.setSpreadFactor(spreadFactor);
        response.setGithubUsername(spreadCalculatorService.getGithubUsername());

        return Collections.singletonList(response);
    }

    private Double extractUsdRate(Map<String, Object> rates) {
        if (rates == null || !rates.containsKey("USD")) {
            return null;
        }
        Object usdValue = rates.get("USD");
        if (usdValue instanceof Double) {
            return (Double) usdValue;
        } else if (usdValue instanceof Integer) {
            return ((Integer) usdValue).doubleValue();
        }
        return null;
    }
}
