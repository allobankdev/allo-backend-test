package test.allo.backend.client;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Slf4j
@Service
@RequiredArgsConstructor
public class FrankFurtherApiClient {

    @Value("${external.frankfurter.endpoint.latest-idr-rates}")
    String uriLatestIdrRate;

    @Value("${external.frankfurter.endpoint.historical-idr-usd}")
    String uriHistoricalIdrUsd;

    @Value("${external.frankfurter.endpoint.supported-currencies}")
    String uriSupportedCurrency;

    private final WebClient webClient;

    public JsonNode fetchLatestIdrRates() {
        JsonNode latestIdrRates = webClient.get()
                .uri(uriLatestIdrRate)
                .retrieve()
                .bodyToMono(JsonNode.class)
                .block();

        log.info("latestIdrRates response: {}", latestIdrRates);
        return latestIdrRates;
    }

    public JsonNode fetchHistoricalIdrUsd() {
        JsonNode historicalIdrUsd = webClient.get()
                .uri(uriHistoricalIdrUsd)
                .retrieve()
                .bodyToMono(JsonNode.class)
                .block();

        log.info("HistoricalIdrUsd response: {}", historicalIdrUsd);
        return historicalIdrUsd;
    }

    public JsonNode fetchSupportedCurrencies() {
        JsonNode supportedCurrencies = webClient.get()
                .uri(uriSupportedCurrency)
                .retrieve()
                .bodyToMono(JsonNode.class)
                .block();

        log.info("supportedCurrencies response: {}", supportedCurrencies);
        return supportedCurrencies;
    }
}
