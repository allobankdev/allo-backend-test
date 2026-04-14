package com.thasya.frankfurter.client;

import com.thasya.frankfurter.dto.FrankfurterLatestResponse;
import com.thasya.frankfurter.dto.FrankfurterTimeseriesResponse;
import com.thasya.frankfurter.exception.ExternalApiException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import org.springframework.http.HttpStatusCode;


import java.time.LocalDate;
import java.util.Map;

@Component
public class FrankfurterClient {

    private static final Logger log = LoggerFactory.getLogger(FrankfurterClient.class);
    private final WebClient webClient;

    public FrankfurterClient(WebClient webClient) {
        this.webClient = webClient;
    }

    public FrankfurterLatestResponse getLatestIdrRates() {
        try {
            FrankfurterLatestResponse response = webClient.get()
                    .uri(uriBuilder -> uriBuilder
                            .path("/latest")
                            .queryParam("base", "IDR")
                            .build())
                    .retrieve()
                    .onStatus(HttpStatusCode::isError, clientResponse ->
                            clientResponse.bodyToMono(String.class)
                                    .defaultIfEmpty("Unknown error")
                                    .flatMap(body -> Mono.error(new ExternalApiException("Error from Frankfurter /latest: " + body)))
                    )
                    .bodyToMono(FrankfurterLatestResponse.class)
                    .block();
            log.info("Successfully fetched latest IDR rates: {}", response != null ? "OK" : "NULL");
            return response;
        } catch (Exception e) {
            log.error("Failed to fetch latest IDR rates: {}", e.getMessage(), e);
            return null;
        }
    }

    public FrankfurterTimeseriesResponse getHistoricalIdrUsd(LocalDate start, LocalDate end) {
        try {
            String path = "/" + start + ".." + end;
            FrankfurterTimeseriesResponse response = webClient.get()
                    .uri(uriBuilder -> uriBuilder
                            .path(path)
                            .queryParam("from", "IDR")
                            .queryParam("to", "USD")
                            .build())
                    .retrieve()
                    .onStatus(HttpStatusCode::isError, clientResponse ->
                            clientResponse.bodyToMono(String.class)
                                    .defaultIfEmpty("Unknown error")
                                    .flatMap(body -> Mono.error(new ExternalApiException("Error from Frankfurter timeseries: " + body)))
                    )
                    .bodyToMono(FrankfurterTimeseriesResponse.class)
                    .block();
            log.info("Successfully fetched historical IDR-USD data: {}", response != null ? "OK" : "NULL");
            return response;
        } catch (Exception e) {
            log.error("Failed to fetch historical IDR-USD data: {}", e.getMessage(), e);
            return null;
        }
    }

    @SuppressWarnings("unchecked")
    public Map<String, String> getCurrencies() {
        try {
            Map<String, String> response = webClient.get()
                    .uri("/currencies")
                    .retrieve()
                    .onStatus(HttpStatusCode::isError, clientResponse ->
                            clientResponse.bodyToMono(String.class)
                                    .defaultIfEmpty("Unknown error")
                                    .flatMap(body -> Mono.error(new ExternalApiException("Error from Frankfurter /currencies: " + body)))
                    )
                    .bodyToMono(Map.class)
                    .block();
            log.info("Successfully fetched currencies: {}", response != null && !response.isEmpty() ? response.size() + " currencies" : "NULL or EMPTY");
            return response;
        } catch (Exception e) {
            log.error("Failed to fetch currencies: {}", e.getMessage(), e);
            return null;
        }
    }
}
