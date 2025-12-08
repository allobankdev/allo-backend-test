package com.tes.allo.fetcher;

import com.tes.allo.dto.CurrenciesDto;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Map;

public class SupportedCurrenciesFetcher implements IDRDataFetcher {

    private final WebClient webClient;

    public SupportedCurrenciesFetcher(WebClient webClient) { this.webClient = webClient; }

    @Override
    public Object fetch() {
        Map<String, String> resp = webClient.get()
                .uri("/currencies")
                .retrieve()
                .bodyToMono(Map.class)
                .block();

        if (resp == null) throw new RuntimeException("Empty currencies response");
        return new CurrenciesDto(resp);
    }

    @Override
    public String key() { return "supported_currencies"; }
}
