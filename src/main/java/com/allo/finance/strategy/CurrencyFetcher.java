
package com.allo.finance.strategy;

import java.time.Duration;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
public class CurrencyFetcher implements IDRDataFetcher {

    private final WebClient client;

    public CurrencyFetcher(WebClient client) {
        this.client = client;
    }

    public String getType() { return "supported_currencies"; }

    public Object fetch() {
        try {
            return client.get().uri("/currencies")
                    .retrieve()
                    .bodyToMono(Object.class)
                    .timeout(Duration.ofSeconds(10))
                    .block();
        } catch (Exception e) {

            return Map.of(
                    "error", "Failed to fetch latest rates",
                    "message", e.getMessage()
            );
        }
    }
}
