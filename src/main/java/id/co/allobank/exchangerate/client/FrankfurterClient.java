package id.co.allobank.exchangerate.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import lombok.extern.slf4j.Slf4j;

import java.util.Map;

@Slf4j
@Component
public class FrankfurterClient {

    @Autowired
    private WebClient webClient;

    public Map getLatestRates() {
        Map result = webClient.get()
                .uri("/latest?base=IDR")
                .retrieve()
                .bodyToMono(Map.class)
                .block();

        return result;
    }

    public Map getHistoricalRates() {

        try {
            Map result = webClient.get()
                .uri(uriBuilder -> uriBuilder
                    .path("/2024-01-01..2024-01-05")
                    .queryParam("from", "IDR")
                    .queryParam("to", "USD")
                    .build()
                )
                .exchangeToMono(response -> {
                    log.info("STATUS CODE: {}", response.statusCode());
                    log.info("HEADERS: {}", response.headers().asHttpHeaders());

                    return response.bodyToMono(Map.class)
                        .map(body -> {
                            log.info("RAW BODY: {}", body);
                            return body;
                        });
                })
                .block();

            return result;
    
        } catch (Exception ex) {
            log.error("Failed to fetch historical rates", ex);
            throw new RuntimeException("Failed to fetch historical rates", ex);
        }
    }

    public Map getCurrencies() {
        Map result = webClient.get()
                .uri("/currencies")
                .retrieve()
                .bodyToMono(Map.class)
                .block();

        return result;
    }
}