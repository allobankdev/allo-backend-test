package com.allobank.finance.strategy;

import com.allobank.finance.client.model.LatestRate;
import com.allobank.finance.config.FinanceApiProperties;
import com.allobank.finance.model.LatestRateData;
import com.allobank.finance.support.SpreadCalculator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.springframework.web.client.RestClient;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Slf4j
@Component
@RequiredArgsConstructor
public class LatestIDRRatesFetcher implements IDRDataFetcher {

    private final RestClient restClient;

    private final FinanceApiProperties properties;

    private final SpreadCalculator spreadCalculator;

    @Override
    public String getResourceType() {
        return "latest_idr_rates";
    }

    @Override
    public LatestRateData fetchData() {
        log.debug("Fetching latest IDR rates");

        LatestRate response = restClient.get()
                .uri("/latest?base=IDR")
                .retrieve()
                .body(LatestRate.class);

        Assert.notNull(response, "Failed to fetch latest IDR rates");

        BigDecimal usdRate = response.rates().get("USD");

        LatestRateData.LatestRateDataBuilder latestRateDataBuilder = LatestRateData.builder();
        populateResult(latestRateDataBuilder, response);

        if (usdRate != null) {
            BigDecimal spreadFactor = spreadCalculator.calculate(properties.getGithub().getUsername());
            BigDecimal usdBuySpreadIDR = computeEffectiveRate(usdRate, spreadFactor);
            latestRateDataBuilder.spreadFactor(spreadFactor);
            latestRateDataBuilder.usdBuySpreadIDR(usdBuySpreadIDR);
        }

        return latestRateDataBuilder.build();
    }

    private void populateResult(LatestRateData.LatestRateDataBuilder latestRateDataBuilder, LatestRate response) {
        latestRateDataBuilder.base(response.base());
        latestRateDataBuilder.date(response.date());
        latestRateDataBuilder.amount(response.amount());
        latestRateDataBuilder.rates(response.rates());
    }

    private BigDecimal computeEffectiveRate(BigDecimal usdRate, BigDecimal spreadFactor) {
        return BigDecimal.ONE
                .divide(usdRate, 10, RoundingMode.HALF_UP)
                .multiply(BigDecimal.ONE.add(spreadFactor));
    }
}