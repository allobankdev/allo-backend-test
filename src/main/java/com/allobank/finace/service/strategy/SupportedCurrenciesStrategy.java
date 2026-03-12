package com.allobank.finace.service.strategy;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Slf4j
@Component("supported_currencies")
public class SupportedCurrenciesStrategy implements IDRDataFetcher {

    private static final String RESOURCE_TYPE = "supported_currencies";

    @Override
    public String getResourceType() {
        return RESOURCE_TYPE;
    }

    @Override
    public List<Map<String, Object>> fetch(WebClient webClient) {
        log.info("Fetching supported currencies from Frankfurter API");

        Map<String, String> response = webClient.get()
                .uri("/currencies")
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<Map<String, String>>() {})
                .block();

        if (response == null) {
            log.warn("Received null response for supported currencies");
            return List.of();
        }

        List<Map<String, Object>> result = new ArrayList<>();
        response.forEach((code, name) ->
                result.add(Map.of("code", code, "name", name))
        );

        result.sort((a, b) -> ((String) a.get("code")).compareTo((String) b.get("code")));

        return result;
    }
}
