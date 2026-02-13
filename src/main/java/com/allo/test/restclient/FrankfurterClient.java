package com.allo.test.restclient;

import com.allo.test.config.FrankfurterProperties;
import com.allo.test.dto.response.HistoricalIdrUsdResponse;
import com.allo.test.dto.response.LatestIDRRatesResponse;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Service
public class FrankfurterClient {

    private final RestTemplate restTemplate;
    private final FrankfurterProperties properties;

    public FrankfurterClient(RestTemplate restTemplate,
                             FrankfurterProperties properties) {
        this.restTemplate = restTemplate;
        this.properties = properties;
    }

    public LatestIDRRatesResponse getLatestRates() {
        String url = properties.getBaseUrl().concat(properties.getEndpoints().getLatest());

        return restTemplate.getForObject(url, LatestIDRRatesResponse.class);
    }

    public HistoricalIdrUsdResponse getHistorical() {
        String url = properties.getBaseUrl().concat(properties.getEndpoints().getHistorical());

        return restTemplate.getForObject(url, HistoricalIdrUsdResponse.class);
    }

    public Map<String, String> getSupportedCurrency() {
        String url = properties.getBaseUrl().concat(properties.getEndpoints().getCurrencies());
        return restTemplate.exchange(
                url,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<Map<String, String>>() {}
        ).getBody();
    }
}
