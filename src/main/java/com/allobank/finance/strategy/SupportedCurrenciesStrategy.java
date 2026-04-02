package com.allobank.finance.strategy;

import com.allobank.finance.repository.FinanceDataRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatusCode;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service("supported_currencies")
public class SupportedCurrenciesStrategy implements IDRDataFetcher {

    private static final Logger log = LoggerFactory.getLogger(SupportedCurrenciesStrategy.class);

    private final WebClient webClient;
    private final FinanceDataRepository financeDataRepository;

    public SupportedCurrenciesStrategy(WebClient webClient, FinanceDataRepository financeDataRepository) {
        this.webClient = webClient;
        this.financeDataRepository = financeDataRepository;
    }

    @Override
    public void fetchAndCacheData() {
        try {
            Map<String, String> response = fetchSupportedCurrencies();

            if (response != null) {
                Map<String, Object> result = new LinkedHashMap<>();
                result.put("resourceType", getResourceType());
                result.put("data", new LinkedHashMap<>(response));
                financeDataRepository.saveData(getResourceType(), List.of(result));
            }
        } catch (Exception e) {
            log.error("Error fetching supported currencies", e);
        }
    }

    @Override
    public Optional<List<Map<String, Object>>> getData() {
        return financeDataRepository.findDataByResourceType(getResourceType());
    }

    @Override
    public String getResourceType() {
        return "supported_currencies";
    }

    private Map<String, String> fetchSupportedCurrencies() {
        return webClient.get()
                .uri("/currencies")
                .retrieve()
                .onStatus(HttpStatusCode::isError, response -> response.bodyToMono(String.class)
                        .defaultIfEmpty("")
                        .map(body -> new IllegalStateException("Frankfurter currencies request failed with status " + response.statusCode().value())))
                .bodyToMono(new ParameterizedTypeReference<Map<String, String>>() {})
                .block();
    }
}
