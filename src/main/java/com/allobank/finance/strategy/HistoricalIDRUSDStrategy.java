package com.allobank.finance.strategy;

import com.allobank.finance.model.FrankfurterHistoricalResponse;
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

@Service("historical_idr_usd")
public class HistoricalIDRUSDStrategy implements IDRDataFetcher {

    private static final Logger log = LoggerFactory.getLogger(HistoricalIDRUSDStrategy.class);

    private final WebClient webClient;
    private final FinanceDataRepository financeDataRepository;

    public HistoricalIDRUSDStrategy(WebClient webClient, FinanceDataRepository financeDataRepository) {
        this.webClient = webClient;
        this.financeDataRepository = financeDataRepository;
    }

    @Override
    public void fetchAndCacheData() {
        try {
            FrankfurterHistoricalResponse response = fetchHistoricalRates();

            if (response != null && response.getRates() != null) {
                Map<String, Object> payload = new LinkedHashMap<>();
                payload.put("amount", response.getAmount());
                payload.put("base", response.getBase());
                payload.put("start_date", response.getStart_date());
                payload.put("end_date", response.getEnd_date());
                payload.put("rates", new LinkedHashMap<>(response.getRates()));

                Map<String, Object> result = new LinkedHashMap<>();
                result.put("resourceType", getResourceType());
                result.put("data", payload);

                financeDataRepository.saveData(getResourceType(), List.of(result));
            }
        } catch (Exception e) {
            log.error("Error fetching historical IDR-USD data", e);
        }
    }

    @Override
    public Optional<List<Map<String, Object>>> getData() {
        return financeDataRepository.findDataByResourceType(getResourceType());
    }

    @Override
    public String getResourceType() {
        return "historical_idr_usd";
    }

    private FrankfurterHistoricalResponse fetchHistoricalRates() {
        return webClient.get()
                .uri("/2024-01-01..2024-01-05?from=IDR&to=USD")
                .retrieve()
                .onStatus(HttpStatusCode::isError, response -> response.bodyToMono(String.class)
                        .defaultIfEmpty("")
                        .map(body -> new IllegalStateException("Frankfurter historical request failed with status " + response.statusCode().value())))
                .bodyToMono(FrankfurterHistoricalResponse.class)
                .block();
    }
}
