package com.allobank.financeapi.service.strategy;

import com.allobank.financeapi.model.enums.ResourceType;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.Map;

@Component
public class SupportedCurrenciesStrategy implements DataFetcherStrategy {

    private final WebClient webClient;

    public SupportedCurrenciesStrategy(@Qualifier("frankfurterWebClient") WebClient webClient) {
        this.webClient = webClient;
    }

    @Override
    public ResourceType getResourceType() {
        return ResourceType.SUPPORTED_CURRENCIES;
    }

    @Override
    public Mono<Object> fetchData() {
        return this.webClient.get()
                .uri("/currencies")
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<Map<String, String>>() {})
                .map(data -> (Object) data);
    }
}
