package com.allo.strategy;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.allo.dto.FinanceResourceResponse;
import com.allo.exception.ExternalApiException;

@Component("historical_idr_usd")
public class HistoricalIDRUSDFetcher implements IDRFetcher {

    private static final Logger log = LoggerFactory.getLogger(HistoricalIDRUSDFetcher.class);
    private static final String DEFAULT_START_DATE = "2024-01-01";
    private static final String DEFAULT_END_DATE = "2024-01-05";

    private final RestTemplate restTemplate;

    public HistoricalIDRUSDFetcher(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    public String resourceType() {
        return "historical_idr_usd";
    }

    @Override
    public List<FinanceResourceResponse> fetch() {
        return fetchByRange(DEFAULT_START_DATE, DEFAULT_END_DATE);
    }

    public List<FinanceResourceResponse> fetchByRange(String startDate, String endDate) {
        log.info("Fetching historical IDR/USD rates from {} to {}", startDate, endDate);
        try {
            String url = "/" + startDate + ".." + endDate + "?from=IDR&to=USD";
            ResponseEntity<Map<String, Object>> response = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<>() {
                    }
            );

            Map<String, Object> body = response.getBody();
            if (body == null) {
                throw new ExternalApiException("Empty response from Frankfurter API for historical data");
            }

            return List.of(new FinanceResourceResponse(resourceType(), body));

        } catch (RestClientException ex) {
            throw new ExternalApiException("Failed to fetch historical IDR/USD rates: " + ex.getMessage(), ex);
        }
    }
}
