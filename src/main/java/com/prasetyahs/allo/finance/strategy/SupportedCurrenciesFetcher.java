package com.prasetyahs.allo.finance.strategy;

import com.prasetyahs.allo.finance.model.CurrencyEntry;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component("supported_currencies")
public class SupportedCurrenciesFetcher implements IDRDataFetcher {

    @Override
    public String getResourceType() {
        return "supported_currencies";
    }

    @Override
    public Object fetchAndProcess(WebClient client) {
        Map<String, String> response = client.get()
                .uri("/currencies")
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<Map<String, String>>() {
                })
                .block();

        if (response == null) {
            return new ArrayList<>();
        }

        return response.entrySet().stream()
                .map(entry -> new CurrencyEntry(entry.getKey(), entry.getValue()))
                .collect(Collectors.toList());
    }
}
