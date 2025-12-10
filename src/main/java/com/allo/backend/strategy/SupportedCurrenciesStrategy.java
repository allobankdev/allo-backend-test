package com.allo.backend.strategy;

import com.allo.backend.exception.ExternalApiException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class SupportedCurrenciesStrategy implements IDRDataFetcher {

    private final WebClient webClient;

    @Override
    public String getResourceType() {
        return "supported_currencies";
    }

    @Override
    public Object fetchData() {
        log.info("Fetching supported currencies from external API");

        try {
            Map<String, String> currencies = webClient.get()
                    .uri("/currencies")
                    .retrieve()
                    .bodyToMono(Map.class)
                    .onErrorResume(e -> {
                        log.error("Error fetching currencies: {}", e.getMessage());
                        return Mono.error(new ExternalApiException("Failed to fetch currencies", e));
                    })
                    .block();

            if (currencies == null) {
                throw new ExternalApiException("Received null response from external API");
            }

            log.info("Successfully fetched {} supported currencies", currencies.size());
            return currencies;

        } catch (Exception e) {
            log.error("Failed to fetch supported currencies: {}", e.getMessage(), e);
            throw new ExternalApiException("Failed to fetch supported currencies", e);
        }
    }
}
