package com.finance.client;

import com.finance.dto.RateResponse;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Mono;

import java.net.URI;

@Component
public class FrankfurterClient{

    private final WebClient webClient;

    public FrankfurterClient(@Qualifier("frankClient") WebClient webClient) {
        this.webClient = webClient;
    }
    public Mono<RateResponse> getLatestRates(String base) {
        URI uri = UriComponentsBuilder
                .fromUriString("https://api.frankfurter.app/latest")
                .queryParam("base", base)
                .build()
                .toUri();

        System.out.println("\nURI = " + uri);

        return webClient.get()
                .uri(uri)
                .retrieve()
                .bodyToMono(RateResponse.class)
                .doOnNext(body -> System.out.println("RAW BODY = " + body));
    }
}

