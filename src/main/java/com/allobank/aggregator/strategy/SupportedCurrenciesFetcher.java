package com.allobank.aggregator.strategy;

import com.allobank.aggregator.dto.FinanceDataDto;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.HashMap;
import java.util.Map;

@Component
@Qualifier("supported_currencies")
public class SupportedCurrenciesFetcher implements IDRDataFetcher {

    private final WebClient webClient;

    public SupportedCurrenciesFetcher(WebClient webClient) {
        this.webClient = webClient;
    }

    @Override
    public String resourceKey() {
        return "supported_currencies";
    }

    @SuppressWarnings("unchecked")
    @Override
    public FinanceDataDto fetch() {
        Map<String, String> resp = webClient.get()
                .uri("/currencies")
                .retrieve()
                .onStatus(s -> s.is4xxClientError() || s.is5xxServerError(),
                        clientResponse -> clientResponse.createException().flatMap(ex -> reactor.core.publisher.Mono.error(new RuntimeException("Frankfurter returned error: " + clientResponse.statusCode()))))
                .bodyToMono(Map.class)
                .block();

        Map<String, Object> out = new HashMap<>();
        out.put("currencies", resp);
        return new FinanceDataDto(resourceKey(), out);
    }
}
