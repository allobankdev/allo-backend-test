package com.api.allorestapi.strategy;

import com.api.allorestapi.model.FinanceDataResponse;
import com.api.allorestapi.model.ResourceType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Component
@RequiredArgsConstructor
public class SupportedCurrenciesFetcher implements IDRDataFetch {

    private final WebClient frankfurterWebClient;

    @Override
    public String getResourceType() {
        return ResourceType.SUPPORTED_CURRENCIES.getValue();
    }

    @Override
    @SuppressWarnings("unchecked")
    public Mono<FinanceDataResponse> fetch() {
        log.debug("Strategy fetch: supported_currencies");
        return frankfurterWebClient
                .get()
                .uri("/currencies")
                .retrieve()
                .bodyToMono(Map.class)
                .map(raw -> {
                    Map<String, String> currencies = (Map<String, String>) raw;

                    List<Object> data = currencies.entrySet().stream()
                            .sorted(Map.Entry.comparingByKey())
                            .map(e -> {
                                Map<String, String> item = new LinkedHashMap<>();
                                item.put("code", e.getKey());
                                item.put("name", e.getValue());
                                return (Object) item;
                            })
                            .collect(Collectors.toList());

                    return FinanceDataResponse.builder()
                            .resourceType(getResourceType())
                            .data(data)
                            .build();
                });
    }
}
