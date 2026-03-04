
package com.allo_backend_test.finance.adapter;

import com.allo_backend_test.finance.Utils.Const;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Component
@RequiredArgsConstructor
public class HistoricalIdrUsdFetcher implements IDRDataFetcher {

    private final RestTemplate restTemplate;

    @Override
    public String getResourceType() {
        return Const.HISTORICAL_IDR_USD;
    }

    @Override
    public Object fetchAndTransform() {
        return restTemplate.getForObject(
                "/2026-03-01..2026-03-04?from=IDR&to=USD",
                Map.class
        );
    }
}
