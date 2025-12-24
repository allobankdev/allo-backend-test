package com.allo.finance.client;

import com.allo.finance.dto.FrankfurterResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Component
@RequiredArgsConstructor
public class FrankfurterClient {

    private final WebClient webClient;

    public FrankfurterResponse getLatestRate(
            String from,
            String to,
            Double amount
    ) {
        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/latest")
                        .queryParam("from", from)
                        .queryParam("to", to)
                        .queryParam("amount", amount)
                        .build()
                )
                .retrieve()
                .bodyToMono(FrankfurterResponse.class)
                .block();
    }

}