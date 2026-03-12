package com.musfiul.idrrateaggregator.service.fetcher.impl;

import com.musfiul.idrrateaggregator.client.FrankfurterClient;
import com.musfiul.idrrateaggregator.constant.ResourceType;
import com.musfiul.idrrateaggregator.dto.LatestRatesResponse;
import com.musfiul.idrrateaggregator.service.fetcher.IDRDataFetcher;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class LatestIdrRatesFetcherImpl implements IDRDataFetcher {
    private static final String LATEST_IDR_RATES = "latest_idr_rates";
    private final FrankfurterClient client;

    @Value("${app.github.username}")
    private String username;

    @Override
    public ResourceType getType() {
        return ResourceType.from(LATEST_IDR_RATES);
    }

    @Override
    public LatestRatesResponse fetchData() {
        LatestRatesResponse response = client.getLatestRates();

        Map<String, BigDecimal> rates = response.getRates();

        BigDecimal rateUsd = rates.get("USD");

        BigDecimal usdBuySpread =
                calculateSpread(username, rateUsd);

        response.setUsdBuySpreadIdr(usdBuySpread);

        return response;
    }

    private BigDecimal calculateSpread(String username, BigDecimal rateUsd) {

        int sum = username.toLowerCase()
                .chars()
                .sum();
        BigDecimal mod = BigDecimal.valueOf(sum)
                .remainder(BigDecimal.valueOf(1000L));

        BigDecimal spreadFactor = mod
                .divide(BigDecimal.valueOf(100000L));

        log.info("username : {}, spreadFactor : {}", username, spreadFactor.toPlainString());
        return BigDecimal.ONE
                .divide(rateUsd, 10, RoundingMode.HALF_UP)
                .multiply(BigDecimal.ONE.add(spreadFactor));
    }
}
