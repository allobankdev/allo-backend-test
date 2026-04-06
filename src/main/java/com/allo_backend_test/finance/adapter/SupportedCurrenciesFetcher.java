
package com.allo_backend_test.finance.adapter;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Component
@RequiredArgsConstructor
public class SupportedCurrenciesFetcher implements IDRDataFetcher {

    private final RestTemplate restTemplate;

    @Override
    public String getResourceType() {
        return "supported_currencies";
    }

    @Override
    public Object fetchAndTransform() {
        return restTemplate.getForObject("/currencies", Map.class);
    }
}
