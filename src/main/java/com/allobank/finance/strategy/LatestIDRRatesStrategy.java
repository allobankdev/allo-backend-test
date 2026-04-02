package com.allobank.finance.strategy;

import com.allobank.finance.model.FrankfurterResponse;
import com.allobank.finance.repository.FinanceDataRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service("latest_idr_rates")
public class LatestIDRRatesStrategy implements IDRDataFetcher {

    private static final Logger log = LoggerFactory.getLogger(LatestIDRRatesStrategy.class);

    private final WebClient webClient;
    private final FinanceDataRepository financeDataRepository;

    private static final String GITHUB_USERNAME = "devsid";

    public LatestIDRRatesStrategy(WebClient webClient, FinanceDataRepository financeDataRepository) {
        this.webClient = webClient;
        this.financeDataRepository = financeDataRepository;
    }

    @Override
    public void fetchAndCacheData() {
        try {
            FrankfurterResponse response = fetchLatestRates();

            if (response != null && response.getRates() != null) {
                Map<String, Object> payload = new LinkedHashMap<>();
                payload.put("amount", response.getAmount());
                payload.put("base", response.getBase());
                payload.put("date", response.getDate());
                payload.put("rates", new LinkedHashMap<>(response.getRates()));
                Double rateUsd = response.getRates().get("USD");
                if (rateUsd != null) {
                    double spreadFactor = calculateSpreadFactor();
                    double usdBuySpreadIdr = (1 / rateUsd) * (1 + spreadFactor);
                    payload.put("USD_BuySpread_IDR", usdBuySpreadIdr);
                    payload.put("spreadFactor", spreadFactor);
                }

                Map<String, Object> result = new LinkedHashMap<>();
                result.put("resourceType", getResourceType());
                result.put("data", payload);

                financeDataRepository.saveData(getResourceType(), List.of(result));
            }
        } catch (Exception e) {
            log.error("Error fetching latest IDR rates", e);
        }
    }

    private double calculateSpreadFactor() {
        int sum = 0;
        for (char c : GITHUB_USERNAME.toCharArray()) {
            sum += c;
        }
        return (sum % 1000) / 100000.0;
    }

    @Override
    public Optional<List<Map<String, Object>>> getData() {
        return financeDataRepository.findDataByResourceType(getResourceType());
    }

    @Override
    public String getResourceType() {
        return "latest_idr_rates";
    }

    private FrankfurterResponse fetchLatestRates() {
        return webClient.get()
                .uri("/latest?base=IDR")
                .retrieve()
                .onStatus(HttpStatusCode::isError, response -> response.bodyToMono(String.class)
                        .defaultIfEmpty("")
                        .map(body -> new IllegalStateException("Frankfurter latest request failed with status " + response.statusCode().value())))
                .bodyToMono(FrankfurterResponse.class)
                .block();
    }
}
