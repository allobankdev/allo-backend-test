package com.allobank.idrrates.strategy;

import com.allobank.idrrates.dto.LatestRateItem;
import com.allobank.idrrates.util.SpreadFactorCalculator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
public class LatestIDRRatesFetcher implements IDRDataFetcher {

    private static final Logger log = LoggerFactory.getLogger(LatestIDRRatesFetcher.class);
    private static final String GITHUB_USERNAME = "andhikakrstn3";
    private static final BigDecimal SPREAD_FACTOR = SpreadFactorCalculator.calculate(GITHUB_USERNAME);

    private final WebClient webClient;

    public LatestIDRRatesFetcher(WebClient webClient) {
        this.webClient = webClient;
    }

    @Override
    public String getResourceType() {
        return "latest_idr_rates";
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<LatestRateItem> fetchAndTransform() {
        log.info("Fetching latest IDR rates from Frankfurter API");

        Map<String, Object> response = webClient.get()
                .uri("/latest?base=IDR")
                .retrieve()
                .bodyToMono(Map.class)
                .block();

        Map<String, Number> rates = (Map<String, Number>) response.get("rates");

        BigDecimal usdRate = rates.containsKey("USD")
                ? new BigDecimal(rates.get("USD").toString())
                : null;

        List<LatestRateItem> items = new ArrayList<>();
        for (Map.Entry<String, Number> entry : rates.entrySet()) {
            String currency = entry.getKey();
            BigDecimal rate = new BigDecimal(entry.getValue().toString());

            BigDecimal usdBuySpreadIdr = null;
            if ("USD".equals(currency) && usdRate != null) {
                BigDecimal idrPerUsd = BigDecimal.ONE.divide(usdRate, 10, RoundingMode.HALF_UP);
                usdBuySpreadIdr = idrPerUsd.multiply(BigDecimal.ONE.add(SPREAD_FACTOR))
                        .setScale(4, RoundingMode.HALF_UP);
            }

            items.add(new LatestRateItem(currency, rate, usdBuySpreadIdr));
        }

        log.info("Fetched {} latest IDR rates", items.size());
        return items;
    }
}
