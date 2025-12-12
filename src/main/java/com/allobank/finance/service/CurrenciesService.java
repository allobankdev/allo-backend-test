package com.allobank.finance.service;

import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.allobank.finance.client.FrankfurterClient;
import com.allobank.finance.dto.CurrenciesResponse;

/**
 * Service for fetching supported currencies from Frankfurter API
 */
@Service
public class CurrenciesService {

    private final FrankfurterClient frankfurterClient;

    public CurrenciesService(FrankfurterClient frankfurterClient) {
        this.frankfurterClient = frankfurterClient;
    }

    /**
     * Fetches the list of supported currencies
     * @return CurrenciesResponse containing currency code to name mappings
     */
    public CurrenciesResponse fetchSupportedCurrencies() {
        WebClient webClient = frankfurterClient.getWebClient();

        CurrenciesResponse response = webClient.get()
            .uri("/currencies")
            .retrieve()
            .bodyToMono(CurrenciesResponse.class)
            .block();

        return response;
    }
}
