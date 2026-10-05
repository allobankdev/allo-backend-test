package com.finance.aggregator.strategy;

import com.finance.aggregator.dto.HistoricalRatesDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import reactor.core.publisher.Mono;
import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class HistoricalRatesStrategy implements DataFetcherStrategy {

    private final RestTemplate restTemplate;

    @Value("${external.api.frankfurter.url}")
    private String baseUrl;

    @Override
    public Mono<Object> fetch() {
        return Mono.fromCallable(() -> {
            String url = baseUrl + "/2024-01-01..2024-01-05?from=IDR&to=USD";
            log.info("Mengambil data historis dari: {}", url);

            Map<String, Object> response = restTemplate.getForObject(url, Map.class);

            if (response == null || response.isEmpty()) {
                throw new RuntimeException("Tidak dapat mengambil data historis dari external API");
            }

            HistoricalRatesDTO dto = new HistoricalRatesDTO();
            dto.setBase((String) response.get("base"));
            dto.setRates((Map<String, Map<String, Double>>) response.get("rates"));
            dto.setResourceType(getType());

            log.info("Berhasil mengambil data historis");
            return dto;
        });
    }

    @Override
    public String getType() {
        return "historical_idr_usd";
    }
}