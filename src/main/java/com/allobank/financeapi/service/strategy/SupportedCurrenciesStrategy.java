package com.allobank.financeapi.service.strategy;

import com.allobank.financeapi.model.FinanceData;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import java.time.LocalDate;
import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class SupportedCurrenciesStrategy implements IDRDataFetcher {

    private final WebClient webClient;

    @Override
    public boolean supports(String resourceType) {
        return "supported_currencies".equals(resourceType);
    }

    @Override
    public String getResourceType() {
        return "supported_currencies";
    }

    @Override
    public Mono<FinanceData> fetchData() {
        log.debug("Fetching supported currencies from Frankfurter API");

        return webClient.get()
                .uri("/currencies")
                .retrieve()
                .bodyToMono(Map.class)
                .map(response -> FinanceData.builder()
                        .resourceType(getResourceType())
                        .data(response)
                        .timestamp(LocalDate.now())
                        .build());
    }
}