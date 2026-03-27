package com.allobank.allo_backend_test.finance.client;

import com.allobank.allo_backend_test.finance.model.dto.HistoricalRatesDto;
import com.allobank.allo_backend_test.finance.model.dto.LatestRateDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class DataSourceClient {

    private final RestClient restClient;

    public LatestRateDto getLatestRates(String base) {
        return restClient.get()
                .uri(u -> u.path("/latest").queryParam("base", base).build())
                .retrieve()
                .body(LatestRateDto.class);
    }

    public HistoricalRatesDto getHistoricalRates(String startDate, String endDate, String from, String to) {
        return restClient.get()
                .uri(u -> u.path("/{range}")
                        .queryParam("from", from)
                        .queryParam("to", to)
                        .build(startDate + ".." + endDate))
                .retrieve()
                .body(HistoricalRatesDto.class);
    }

    public Map<String, String> getCurrencies() {
        return restClient.get()
                .uri("/currencies")
                .retrieve()
                .body(new ParameterizedTypeReference<>() {});
    }
}