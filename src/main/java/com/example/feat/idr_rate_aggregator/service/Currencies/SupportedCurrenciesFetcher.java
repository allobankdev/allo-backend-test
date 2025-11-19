package com.example.feat.idr_rate_aggregator.service.Currencies;

import com.example.feat.idr_rate_aggregator.exception.ExternalApiException;
import com.example.feat.idr_rate_aggregator.service.financeDataStore.IDRDataFetcher;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Map;

@Component("supported_currencies")
public class SupportedCurrenciesFetcher implements IDRDataFetcher {

    private static final String RESOURCE_KEY = "supported_currencies";
    private final WebClient webClient;

    public SupportedCurrenciesFetcher(WebClient webClient) {
        this.webClient = webClient;
    }

    @Override
    public String getResourceKey() {
        return RESOURCE_KEY;
    }

    @Override
    public Object fetchData() {

        ParameterizedTypeReference<Map<String, String>> typeRef =
                new ParameterizedTypeReference<Map<String, String>>() {};

        return webClient.get()
                .uri("/currencies")
                .retrieve()
                .onStatus(status -> status.is4xxClientError() || status.is5xxServerError(),
                        clientResponse -> clientResponse.bodyToMono(String.class).flatMap(body ->
                                reactor.core.publisher.Mono.error(new ExternalApiException("Frankfurter error: " + body))
                        ))
                .bodyToMono(typeRef)
                .block();
    }
}
