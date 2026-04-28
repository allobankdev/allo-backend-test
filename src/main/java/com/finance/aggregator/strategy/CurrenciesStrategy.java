package com.finance.aggregator.strategy;

import com.finance.aggregator.dto.CurrenciesDTO;
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
public class CurrenciesStrategy implements DataFetcherStrategy {

    private final RestTemplate restTemplate;

    @Value("${external.api.frankfurter.url}")
    private String baseUrl;

    @Override
    public Mono<Object> fetch() {
        return Mono.fromCallable(() -> {
            String url = baseUrl + "/currencies";
            log.info("Mengambil daftar mata uang dari: {}", url);

            Map<String, String> response = restTemplate.getForObject(url, Map.class);

            if (response == null || response.isEmpty()) {
                throw new RuntimeException("Tidak dapat mengambil data currencies dari external API");
            }

            CurrenciesDTO dto = new CurrenciesDTO();
            dto.setCurrencies(response);
            dto.setResourceType(getType());

            log.info("Berhasil mengambil {} mata uang", response.size());
            return dto;
        });
    }

    @Override
    public String getType() {
        return "supported_currencies";
    }
}