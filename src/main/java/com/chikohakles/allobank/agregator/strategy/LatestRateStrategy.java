package com.chikohakles.allobank.agregator.strategy;

import com.chikohakles.allobank.agregator.constant.ResourceType;
import com.chikohakles.allobank.agregator.dto.LatestResponse;
import com.chikohakles.allobank.agregator.helper.CalculationUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.math.BigDecimal;
import java.util.Currency;

@RequiredArgsConstructor
@Service
public class LatestRateStrategy implements BaseStrategy {
    private static final String URL_LATEST = "/latest?base={base}";
    private static final Currency IDR = Currency.getInstance("IDR");
    private static final Currency USD = Currency.getInstance("USD");
    @Value("${github.username}")
    private String username;
    private final RestClient restClient;

    @Override
    public ResourceType getResourceType() {
        return ResourceType.LATEST_IDR_RATES;
    }

    @Override
    public Object getData() {
        LatestResponse latestResponse = restClient.get()
                .uri(URL_LATEST, IDR.getCurrencyCode())
                .retrieve()
                .body(LatestResponse.class);
        BigDecimal spreadFactor = CalculationUtil.calculateSpreadFactor(username);
        assert latestResponse != null;
        BigDecimal usdRate = CalculationUtil.calculateRate(spreadFactor, latestResponse.getRates().get(USD.getCurrencyCode()));
        latestResponse.setUSD_BuySpread_IDR(usdRate);
        return latestResponse;
    }
}
