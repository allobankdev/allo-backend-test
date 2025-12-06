package com.bank.allo.client;

import com.bank.allo.repository.outbound.FrankfurterClientRepository;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import java.util.Map;

public class FrankfurterClientRepositoryImpl implements FrankfurterClientRepository {

    private final WebClient webClient;

    public FrankfurterClientRepositoryImpl(WebClient webClient) {
        this.webClient = webClient;
    }

    @Override
    public Map<String, Object> fetchLatestBaseIdr() {
        try {
            Mono<Map> mono = webClient.get()
                    .uri(uri -> uri.path("/latest").queryParam("base", "IDR").build())
                    .retrieve()
                    .bodyToMono(Map.class);

            return mono.block();
        } catch (Exception ex) {
            return Map.of(
                    "base", "IDR",
                    "date", "",
                    "rates", Map.of()
            );
        }
    }

    @Override
    public Map<String, Object> fetchHistoricalIdrUsd() {
        try {
            Mono<Map> mono = webClient.get()
                    .uri(uri -> uri.path("/2024-01-01..2024-01-05")
                            .queryParam("from","IDR")
                            .queryParam("to","USD")
                            .build())
                    .retrieve()
                    .bodyToMono(Map.class);

            return mono.block();
        } catch (Exception ex) {
            return Map.of(
                    "start_date", "2024-01-01",
                    "end_date", "2024-01-05",
                    "rates", Map.of()
            );
        }
    }

    @Override
    public Map<String, String> fetchSupportedCurrencies() {
        try {
            Mono<Map> mono = webClient.get()
                    .uri("/currencies")
                    .retrieve()
                    .bodyToMono(Map.class);

            return mono.block();
        } catch (Exception ex) {
            return Map.of();
        }
    }
}
