package com.hanifnfl.allobank.strategy;

import com.hanifnfl.allobank.dto.FrankfurterLatestResponse;
import com.hanifnfl.allobank.dto.LatestIdrRatesView;
import com.hanifnfl.allobank.util.SpreadFactorCalculator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Map;

@Slf4j
@Component("latest_idr_rates")
public class LatestIdrRatesFetcher implements IDRDataFetcher {

    private volatile List<LatestIdrRatesView> cache = List.of();

    private final String githubUsername;

    public LatestIdrRatesFetcher(@Value("${app.github-username}") String githubUsername) {
        this.githubUsername = githubUsername;
    }

    @Override
    public String getResourceTypeKey() {
        return "latest_idr_rates";
    }

    @Override
    public void loadData(WebClient client) {
        log.info("Fetching latest IDR rates...");

        FrankfurterLatestResponse response = client.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/latest")
                        .queryParam("base", "IDR")
                        .build())
                .retrieve()
                .bodyToMono(FrankfurterLatestResponse.class)
                .block();

        if (response == null || response.rates() == null || !response.rates().containsKey("USD")) {
            throw new IllegalStateException("USD rate not found in latest IDR rates.");
        }

        BigDecimal usdRate = response.rates().get("USD");
        BigDecimal spreadFactor = SpreadFactorCalculator.calculateSpreadFactor(githubUsername);

        BigDecimal inverted = BigDecimal.ONE
                .divide(usdRate, 10, RoundingMode.HALF_UP);
        BigDecimal usdBuySpreadIdr = inverted
                .multiply(BigDecimal.ONE.add(spreadFactor))
                .setScale(4, RoundingMode.HALF_UP);

        LatestIdrRatesView view = new LatestIdrRatesView(
                response.base(),
                response.date().toString(),
                Map.of(
                        "amount", response.amount(),
                        "rates", response.rates()
                ),
                usdBuySpreadIdr,
                spreadFactor
        );

        this.cache = List.of(view);
        log.info("latest_idr_rates loaded. spreadFactor={}, usdBuySpreadIdr={}",
                spreadFactor, usdBuySpreadIdr);
    }

    @Override
    public List<LatestIdrRatesView> getCachedData() {
        return cache;
    }
}
