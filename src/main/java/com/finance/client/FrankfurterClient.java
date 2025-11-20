package com.finance.client;

import com.finance.constant.AppConstant;
import com.finance.dto.external.HistoricalRateResponse;
import com.finance.dto.external.RateResponse;
import com.finance.dto.external.SupportedCurrenciesResponse;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.time.LocalDate;

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

    public Mono<SupportedCurrenciesResponse> getSupportedCurrencies() {
        URI uri = UriComponentsBuilder
                .fromUriString("https://api.frankfurter.app/currencies")
                .build()
                .toUri();

        System.out.println("\nURI = " + uri);

        return webClient.get()
                .uri(uri)
                .retrieve()
                .bodyToMono(SupportedCurrenciesResponse.class)
                .doOnNext(body -> System.out.println("RAW BODY = " + body));
    }

    public Mono<HistoricalRateResponse> getHistoricalIdrUsd() {
        String range = "2024-01-01..2024-01-05"; // fixed range, as the README.md said
        URI uri = UriComponentsBuilder
                .fromUriString("https://api.frankfurter.app/" + range)
                .queryParam("from", AppConstant.IDR_BASE)
                .queryParam("to", AppConstant.USD_BASE)
                .build()
                .toUri();

        System.out.println("\nURI = " + uri);

        return webClient.get()
                .uri(uri)
                .retrieve()
                .bodyToMono(HistoricalRateResponse.class)
                .doOnNext(body -> System.out.println("RAW BODY = " + body));
    }
}

