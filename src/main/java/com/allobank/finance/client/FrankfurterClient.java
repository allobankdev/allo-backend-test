package com.allobank.finance.client;

import com.allobank.finance.dto.HistoricalIdrUsdDto;
import com.allobank.finance.dto.LatestIdrRatesDto;
import com.allobank.finance.dto.SupportedCurrenciesDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Component
@RequiredArgsConstructor
public class FrankfurterClient {

    private final WebClient frankfukterWebClient;

    public HistoricalIdrUsdDto getHistoricalRates() {
        return frankfukterWebClient.get()
                .uri("/2024-01-01..2024-01-05?from=IDR&to=USD")
                .retrieve()
                .bodyToMono(HistoricalIdrUsdDto.class)
                .block();
    }

    public LatestIdrRatesDto getLatestRates() {
        return frankfukterWebClient.get()
                .uri("/latest?base=IDR")
                .retrieve()
                .bodyToMono(LatestIdrRatesDto.class)
                .block();
    }

    public SupportedCurrenciesDto getSupportedCurrencies() {
        return frankfukterWebClient.get()
                .uri("/currencies")
                .retrieve()
                .bodyToMono(SupportedCurrenciesDto.class)
                .block();
    }
}
