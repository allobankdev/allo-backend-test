package com.athallah.finance.client;

import com.athallah.finance.dto.HistoricalRatesRawDto;
import com.athallah.finance.dto.LatestRatesRawDto;
import com.athallah.finance.util.exception.ClientException;
import com.athallah.finance.util.exception.ServerException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.scheduler.Schedulers;
import reactor.util.retry.Retry;

import java.time.Duration;
import java.util.Map;

@Component
@Slf4j
public class FinanceFrankfurterWebClient {

    @Autowired
    private WebClient externalWebClient;

    @Value("${finance.frankfurter.url.latest}")
    private String latestRatesUrl;

    @Value("${finance.frankfurter.url.historical}")
    private String historicalUrl;

    @Value("${finance.frankfurter.url.currencies}")
    private String currenciesUrl;

    public LatestRatesRawDto getLatestIdrRates() throws ClientException, ServerException {
        log.info("[Finance] Fetching latest IDR rates from Frankfurter");

        return externalWebClient.get()
                .uri(latestRatesUrl)
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToMono(LatestRatesRawDto.class)
                .retryWhen(
                        Retry.backoff(3, Duration.ofSeconds(2))
                                .filter(ServerException.class::isInstance)
                                .onRetryExhaustedThrow((retryBackoffSpec, signal) ->
                                        new ServerException("External service failed after max retries",
                                                HttpStatus.SERVICE_UNAVAILABLE.value())
                                )
                )
                .publishOn(Schedulers.boundedElastic())
                .block();
    }

    public HistoricalRatesRawDto getHistoricalIdrUsd() throws ClientException, ServerException {
        log.info("[Finance] Fetching historical IDR→USD data from Frankfurter");

        return externalWebClient.get()
                .uri(historicalUrl)
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToMono(HistoricalRatesRawDto.class)
                .retryWhen(
                        Retry.backoff(3, Duration.ofSeconds(2))
                                .filter(ServerException.class::isInstance)
                                .onRetryExhaustedThrow((retryBackoffSpec, signal) ->
                                        new ServerException("External service failed after max retries",
                                                HttpStatus.SERVICE_UNAVAILABLE.value())
                                )
                )
                .publishOn(Schedulers.boundedElastic())
                .block();
    }

    public Map<String, String> getSupportedCurrencies() throws ClientException, ServerException {
        log.info("[Finance] Fetching supported currencies from Frankfurter");

        return externalWebClient.get()
                .uri(currenciesUrl)
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<Map<String, String>>() {})
                .retryWhen(
                        Retry.backoff(3, Duration.ofSeconds(2))
                                .filter(ServerException.class::isInstance)
                                .onRetryExhaustedThrow((retryBackoffSpec, signal) ->
                                        new ServerException("External service failed after max retries",
                                                HttpStatus.SERVICE_UNAVAILABLE.value())
                                )
                )
                .publishOn(Schedulers.boundedElastic())
                .block();
    }
}
