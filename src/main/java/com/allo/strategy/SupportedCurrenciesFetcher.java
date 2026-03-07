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

@Component("supported_currencies")
public class SupportedCurrenciesFetcher implements IDRFetcher {

    private static final Logger log = LoggerFactory.getLogger(SupportedCurrenciesFetcher.class);

    private final RestTemplate restTemplate;

    public SupportedCurrenciesFetcher(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    public String resourceType() {
        return "supported_currencies";
    }

    @Override
    public List<FinanceResourceResponse> fetch() {
        log.info("Fetching supported currencies from Frankfurter API");
        try {
            ResponseEntity<Map<String, String>> response = restTemplate.exchange(
                    "/currencies",
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<>() {
                    }
            );

            Map<String, String> body = response.getBody();
            if (body == null) {
                throw new ExternalApiException("Empty response from Frankfurter API for currencies");
            }

            return List.of(new FinanceResourceResponse(resourceType(), body));

        } catch (RestClientException ex) {
            throw new ExternalApiException("Failed to fetch supported currencies: " + ex.getMessage(), ex);
        }
    }
}
