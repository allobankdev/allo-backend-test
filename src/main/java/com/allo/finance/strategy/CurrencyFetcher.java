
package com.allo.finance.strategy;

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
        return client.get().uri("/currencies")
                .retrieve().bodyToMono(Object.class).block();
    }
}
