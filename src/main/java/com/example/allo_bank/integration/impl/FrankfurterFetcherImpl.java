package com.example.allo_bank.integration.impl;

import com.example.allo_bank.config.properties.FrankfurterApiPropertiesConfig;
import com.example.allo_bank.config.properties.GithubPropertiesConfig;
import com.example.allo_bank.exception.ExternalApiException;
import com.example.allo_bank.integration.FrankfurterFetcher;
import com.example.allo_bank.integration.dto.HistoricalIdrUsdDto;
import com.example.allo_bank.integration.dto.LatestIdrRatesDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientRequestException;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.util.Map;

@Service
public class FrankfurterFetcherImpl implements FrankfurterFetcher {

    private final WebClient client;

    @Autowired
    private GithubPropertiesConfig githubPropertiesConfig;

    @Autowired
    private FrankfurterApiPropertiesConfig frankfurterApiPropertiesConfig;

    private static final Logger log = LoggerFactory.getLogger(FrankfurterFetcherImpl.class);

    public FrankfurterFetcherImpl(WebClient client) {
        this.client = client;
    }


    @Override
    public LatestIdrRatesDto getLatestIdrRates() {
        try {
            return client.get()
                    .uri(frankfurterApiPropertiesConfig.getLatestIdrRatesPath())
                    .retrieve()
                    .onStatus(HttpStatusCode::isError, response ->
                            response.bodyToMono(String.class)
                                    .defaultIfEmpty(response.statusCode().toString())
                                    .flatMap(body -> {
                                        log.error("External API returned error: {} - {}", response.statusCode(), body);
                                        return Mono.error(new ExternalApiException(
                                                "Failed to fetch getLatestIdrRates : " + response.statusCode() + " - " + body));
                                    })
                    )
                    .bodyToMono(LatestIdrRatesDto.class)
                    .block();

        } catch (WebClientRequestException e) {
            log.error("Network error fetching getLatestIdrRates", e);
            throw new ExternalApiException("Network error fetching getLatestIdrRates", e);
        } catch (Exception e) {
            log.error("Unexpected error fetching getLatestIdrRates", e);
            throw new ExternalApiException("Unexpected error fetching getLatestIdrRates", e);
        }
    }

    @Override
    public HistoricalIdrUsdDto getHistoricalIdrUsd() {
        try {
            return client.get()
                    .uri(frankfurterApiPropertiesConfig.getHistoricalPath())
                    .retrieve()
                    // Handle HTTP 4xx / 5xx errors
                    .onStatus(HttpStatusCode::isError, response ->
                            response.bodyToMono(String.class)
                                    .defaultIfEmpty(response.statusCode().toString())
                                    .flatMap(body -> {
                                        log.error("External API returned error: {} - {}", response.statusCode(), body);
                                        return Mono.error(new ExternalApiException(
                                                "Failed to fetch getHistoricalIdrUsd : " + response.statusCode() + " - " + body));
                                    })
                    )
                    .bodyToMono(HistoricalIdrUsdDto.class)
                    .block();

        } catch (WebClientRequestException e) {
            log.error("Network error fetching getHistoricalIdrUsd", e);
            throw new ExternalApiException("Network error fetching getHistoricalIdrUsd", e);
        } catch (Exception e) {
            log.error("Unexpected error fetching getHistoricalIdrUsd", e);
            throw new ExternalApiException("Unexpected error fetching getHistoricalIdrUsd", e);
        }
    }

    @Override
    public Map<String, String> getSupportedCurrencies() {
        try {
            return client.get()
                    .uri(frankfurterApiPropertiesConfig.getSupportedCurrenciesPath())
                    .retrieve()
                    // Handle HTTP 4xx / 5xx errors
                    .onStatus(HttpStatusCode::isError, response ->
                            response.bodyToMono(String.class)
                                    .defaultIfEmpty(response.statusCode().toString())
                                    .flatMap(body -> {
                                        log.error("External API returned error: {} - {}", response.statusCode(), body);
                                        return Mono.error(new ExternalApiException(
                                                "Failed to fetch getSupportedCurrencies : " + response.statusCode() + " - " + body));
                                    })
                    )
                    .bodyToMono(new ParameterizedTypeReference<Map<String, String>>() {})
                    .block();

        } catch (WebClientRequestException e) {
            log.error("Network error fetching getSupportedCurrencies", e);
            throw new ExternalApiException("Network error fetching getSupportedCurrencies", e);
        } catch (Exception e) {
            log.error("Unexpected error fetching getSupportedCurrencies", e);
            throw new ExternalApiException("Unexpected error fetching getSupportedCurrencies", e);
        }
    }
}
