package io.aditsukoco.allobank_test.services;

import io.aditsukoco.allobank_test.clients.frankfurter.FrankfurterHTTPClientInterface;
import io.aditsukoco.allobank_test.models.dto.api_response.HistoricalDataAPIResponseDTO;
import io.aditsukoco.allobank_test.models.dto.api_response.LatestAPIResponseDTO;
import io.aditsukoco.allobank_test.repositories.frankfurter.FrankfurterDataRepositoryInterface;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.CompletableFuture;

@Slf4j
@Component
@RequiredArgsConstructor
public class AllobankTestApplicationRunner implements ApplicationRunner {

    private final FrankfurterHTTPClientInterface frankfurterHTTPClient;
    private final FrankfurterDataRepositoryInterface frankfurterDataRepository;

    @Override
    public void run(ApplicationArguments args) throws Exception {

        // populate latest response data
        LatestAPIResponseDTO apiResponseLatestData = frankfurterHTTPClient.fetchLatest(1, "IDR", "USD");
        frankfurterDataRepository.setLatestResponseData(apiResponseLatestData);
        log.info("Finished fetching and storing latest data");

        // populate historical response data
        HistoricalDataAPIResponseDTO apiResponseHistoricalData = frankfurterHTTPClient.fetchHistorical("IDR", "USD", "2024-01-01", "2024-01-05");
        frankfurterDataRepository.setHistoricalResponseData(apiResponseHistoricalData);
        log.info("Finished fetching and storing historical data");

        // populate currencies data
        Map<String, String> apiResponseCurrenciesData = frankfurterHTTPClient.fetchCurrencies();
        frankfurterDataRepository.setCurrencies(apiResponseCurrenciesData);
        log.info("Finished fetching and storing currencies data");

    }
}
