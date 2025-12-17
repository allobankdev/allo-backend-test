package com.example.allobank.service;

import com.example.allobank.dto.FinanceDataItemDto;
import com.example.allobank.exception.ExternalServiceException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Component("supported_currencies")
@RequiredArgsConstructor
public class SupportedCurrenciesFetcher implements IDRDataFetcher {

    private final WebClient webClient;

    @Override
    public String resourceType() {
        return "supported_currencies";
    }

    @Override
    public List<FinanceDataItemDto> fetch() {
        Map<String, String> currencies = webClient.get()
                .uri("/currencies")
                .retrieve()
                .onStatus(status -> status.is4xxClientError() || status.is5xxServerError(),
                        resp -> resp.bodyToMono(String.class)
                                .defaultIfEmpty("")
                                .flatMap(body -> Mono.error(new ExternalServiceException(
                                        "Frankfurter /currencies returned " + resp.statusCode() + " body=" + body))))
                .bodyToMono(new ParameterizedTypeReference<Map<String, String>>() {})
                .onErrorMap(ex -> (ex instanceof ExternalServiceException) ? ex :
                        new ExternalServiceException("Failed calling Frankfurter /currencies", ex))
                .block();

        if (currencies == null) {
            throw new ExternalServiceException("Frankfurter /currencies response is empty");
        }

        List<FinanceDataItemDto> items = new ArrayList<>();
        for (Map.Entry<String, String> e : currencies.entrySet()) {
            items.add(FinanceDataItemDto.builder()
                    .resourceType(resourceType())
                    .key(e.getKey())
                    .value(e.getValue())
                    .meta(new LinkedHashMap<>())
                    .build());
        }

        return List.copyOf(items);
    }
}