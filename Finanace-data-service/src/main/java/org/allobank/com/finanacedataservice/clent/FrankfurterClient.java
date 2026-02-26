package org.allobank.com.finanacedataservice.clent;

import org.allobank.com.finanacedataservice.clent.dto.FrankfurterLatestResponse;
import org.allobank.com.finanacedataservice.clent.dto.FrankfurterTimeSeriesResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Component
public class FrankfurterClient {
    private final RestTemplate restTemplate;
    private final String historicalfrom;
    private final String historicalTo;

    public FrankfurterClient(RestTemplate restTemplate,
                             @Value("${app.historical.from}") String historicalfrom,
                             @Value("${app.historical.to}") String historicalTo) {
        this.restTemplate = restTemplate;
        this.historicalfrom = historicalfrom;
        this.historicalTo = historicalTo;
    }

    public FrankfurterLatestResponse getLatestIdrRates() {
        try {
            ResponseEntity<FrankfurterLatestResponse> response = restTemplate.getForEntity("/latest?base=IDR", FrankfurterLatestResponse.class);
            return response.getBody();
        } catch (RestClientException ex) {
            throw  ex;
        }

    }

    public FrankfurterTimeSeriesResponse getHistoricalIdrUsd() {
        try {
            String path = "/" + historicalfrom + ".." + historicalTo + "?from=IDR&to=USD";
            ResponseEntity<FrankfurterTimeSeriesResponse> response = restTemplate.getForEntity(path, FrankfurterTimeSeriesResponse.class);
            return response.getBody();
        } catch (RestClientException ex) {
            throw ex;
        }
    }

    public Map<String, String> getSupportedCurrencies() {
        try {
            ResponseEntity<Map> response = restTemplate.getForEntity("/currencies", Map.class);
            return (Map<String, String>) response.getBody();
        } catch (RestClientException exception) {
            throw exception;
        }
    }
}
