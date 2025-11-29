package com.allobank.backendtest.fetcher;

import com.allobank.backendtest.dto.LatestRateDto;
import com.allobank.backendtest.util.SpreadCalculator;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.*;

public class LatestIdrRatesFetcher implements IDRDataFetcher{
    private final WebClient client;
    private final String githubUsername;

    public LatestIdrRatesFetcher(WebClient client, String githubUsername) {
        this.client = client;
        this.githubUsername = githubUsername;
    }

    @Override
    public String resourceKey() { return "latest_idr_rates"; }

    @Override
    @SuppressWarnings("unchecked")
    public List<LatestRateDto> fetchSync() throws Exception {
        Map<String, Object> resp;
        try {
            resp = client.get()
                    .uri(uriBuilder -> uriBuilder.path("/latest").queryParam("base", "IDR").build())
                    .retrieve()
                    .bodyToMono(Map.class)
                    .block();
        } catch (WebClientResponseException ex) {
            throw new IllegalStateException("Failed fetching latest rates: " + ex.getMessage(), ex);
        }

        if (resp == null || !resp.containsKey("rates")) {
            throw new IllegalStateException("Invalid response for latest rates");
        }

        Map<String, Number> rates = (Map<String, Number>) resp.get("rates");
        Number usdNum = rates.get("USD");
        if (usdNum == null) {
            throw new IllegalStateException("USD rate missing in latest response");
        }
        BigDecimal rateUsd = new BigDecimal(usdNum.toString()); // rate = USD per IDR
        BigDecimal spread = SpreadCalculator.computeSpreadFactor(githubUsername);
        BigDecimal idrPerUsd = BigDecimal.ONE.divide(rateUsd, new MathContext(20, RoundingMode.HALF_UP));
        BigDecimal usdBuySpreadIdr = idrPerUsd
                .multiply(BigDecimal.ONE.add(spread))
                .setScale(6, RoundingMode.HALF_UP);

        LatestRateDto dto = new LatestRateDto("USD", rateUsd, usdBuySpreadIdr);
        return Collections.singletonList(dto);
    }
}
