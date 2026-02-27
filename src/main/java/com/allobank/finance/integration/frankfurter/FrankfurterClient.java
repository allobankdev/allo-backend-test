package com.allobank.finance.integration.frankfurter;

import com.allobank.finance.config.properties.FrankfurterProperties;
import com.allobank.finance.integration.frankfurter.dto.HistoricalRatesRawResponse;
import com.allobank.finance.integration.frankfurter.dto.LatestRatesRawResponse;
import com.allobank.finance.integration.frankfurter.dto.SupportedCurrenciesRawResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Objects;

@Component
@RequiredArgsConstructor
@EnableConfigurationProperties(FrankfurterProperties.class)
public class FrankfurterClient {

    private final FrankfurterProperties frankfurterProperties;
    private final WebClient webClient;

    public LatestRatesRawResponse fetchLatestRates() {
        var base = frankfurterProperties.resources().latest().base();
        var response = webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/latest")
                        .queryParam("base", base)
                        .build())
                .retrieve()
                .bodyToMono(LatestRatesRawResponse.class)
                .block();
        return Objects.requireNonNull(response, "Frankfurter latest response null");
    }

    public HistoricalRatesRawResponse fetchHistoricalRates() {
        var hist = frankfurterProperties.resources().historical();
        var response = webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .pathSegment(hist.range())
                        .queryParam("from", hist.from())
                        .queryParam("to", hist.to())
                        .build())
                .retrieve()
                .bodyToMono(HistoricalRatesRawResponse.class)
                .block();
        return Objects.requireNonNull(response, "Frankfurter historical response null");
    }

    public SupportedCurrenciesRawResponse fetchSupportedCurrencies() {
        var response = webClient.get()
                .uri("/currencies")
                .retrieve()
                .bodyToMono(SupportedCurrenciesRawResponse.class)
                .block();
        return Objects.requireNonNull(response, "Frankfurter currencies response null");
    }
}
