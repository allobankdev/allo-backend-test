package test.allo.backend;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import test.allo.backend.storage.InMemoryStorage;

import static test.allo.backend.utils.ConstantUtils.*;

@Slf4j
@Component
@RequiredArgsConstructor
public class StartupLoader implements ApplicationRunner {

    private final WebClient webClient;
    private final InMemoryStorage storage;

    @Value("${external.frankfurter.endpoint.latest-idr-rates}")
    String uriLatestIdrRate;

    @Value("${external.frankfurter.endpoint.historical-idr-usd}")
    String uriHistoricalIdrUsd;

    @Value("${external.frankfurter.endpoint.supported-currencies}")
    String uriSupportedCurrency;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        JsonNode latestIdrRates = webClient.get()
                .uri(uriLatestIdrRate)
                .retrieve()
                .bodyToMono(JsonNode.class)
                .block();
        log.info("latestIdrRates response: {}", latestIdrRates);
        if (latestIdrRates != null) storage.save(LATEST_IDR_RATE, latestIdrRates);

        JsonNode historicalIdrUsd = webClient.get()
                .uri(uriHistoricalIdrUsd)
                .retrieve()
                .bodyToMono(JsonNode.class)
                .block();
        log.info("HistoricalIdrUsd response: {}", historicalIdrUsd);
        if (historicalIdrUsd != null) storage.save(HISTORICAL_IDR_USD, historicalIdrUsd);

        JsonNode supportedCurrencies = webClient.get()
                .uri(uriSupportedCurrency)
                .retrieve()
                .bodyToMono(JsonNode.class)
                .block();
        log.info("supportedCurrencies response: {}", supportedCurrencies);
        if(supportedCurrencies != null) storage.save(SUPPORTED_CURRENCIES, supportedCurrencies);

        storage.lockStorage();

    }
}
