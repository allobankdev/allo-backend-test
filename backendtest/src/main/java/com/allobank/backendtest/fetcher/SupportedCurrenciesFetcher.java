package com.allobank.backendtest.fetcher;

import com.allobank.backendtest.dto.SupportedCurrenciesDto;
import org.springframework.web.reactive.function.client.WebClient;
import java.util.*;

public class SupportedCurrenciesFetcher implements IDRDataFetcher {
    private final WebClient client;
    public SupportedCurrenciesFetcher(WebClient client) { this.client = client; }

    @Override public String resourceKey() { return "supported_currencies"; }

    @Override
    @SuppressWarnings("unchecked")
    public List<SupportedCurrenciesDto> fetchSync() throws Exception {
        Map<String, String> resp = client.get()
                .uri("/currencies")
                .retrieve()
                .bodyToMono(Map.class)
                .block();

        if (resp == null) return Collections.emptyList();
        List<SupportedCurrenciesDto> list = new ArrayList<>();
        resp.forEach((k,v) -> list.add(new SupportedCurrenciesDto(k, v)));
        list.sort(Comparator.comparing(SupportedCurrenciesDto::code));
        return list;
    }
}
