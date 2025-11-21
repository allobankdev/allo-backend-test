package com.chikohakles.allobank.agregator.strategy;

import com.chikohakles.allobank.agregator.constant.ResourceType;
import com.chikohakles.allobank.agregator.dto.LatestResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.Currency;

@RequiredArgsConstructor
@Service
public class LatestRateStrategy implements BaseStrategy {
    private static final String URL_LATEST = "/latest?base={base}";
    private static final Currency IDR = Currency.getInstance("IDR");
    private final RestClient restClient;

    @Override
    public ResourceType getResourceType() {
        return ResourceType.LATEST_IDR_RATES;
    }

    @Override
    public Object getData() {
        return restClient.get()
                .uri(URL_LATEST, IDR.getCurrencyCode())
                .retrieve()
                .body(LatestResponse.class);
    }
}
