package allobankdev.test.finance.client;

import org.springframework.web.client.RestTemplate;

import java.util.Map;

public class FrankfurterClient {

    private final RestTemplate restTemplate;
    private final String baseUrl;

    public FrankfurterClient(RestTemplate restTemplate, String baseUrl) {
        this.restTemplate = restTemplate;
        this.baseUrl = baseUrl;
    }

    public Map<String, Object> getLatestIdrRates() {
        return restTemplate.getForObject(
                baseUrl + "/latest?base=IDR",
                Map.class
        );
    }

    public Map<String, Object> getHistoricalIdrUsd() {
        return restTemplate.getForObject(
                baseUrl + "/2024-01-01..2024-01-05?from=IDR&to=USD",
                Map.class
        );
    }

    public Map<String, Object> getCurrencies() {
        return restTemplate.getForObject(
                baseUrl + "/currencies",
                Map.class
        );
    }
}

