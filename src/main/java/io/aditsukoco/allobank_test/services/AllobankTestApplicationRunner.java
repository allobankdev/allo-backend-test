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
import org.springframework.web.client.RestClientException;

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
        CompletableFuture.runAsync(this::populateLatestResponseData);
        CompletableFuture.runAsync(this::populateHistoricalResponseData);
        CompletableFuture.runAsync(this::populateCurrenciesResponseData);
    }

    private void populateLatestResponseData() {
        // populate latest response data
        boolean isFailed = false;
        while (true) {
            try {
                log.info("Fetching and storing latest data....");
                LatestAPIResponseDTO apiResponseLatestData = frankfurterHTTPClient.fetchLatest(1, "IDR", "USD");
                frankfurterDataRepository.setLatestResponseData(apiResponseLatestData);
                log.info("Finished fetching and storing latest data");
            } catch (RestClientException e) {
                log.error("An error occurred while fetching latest data, retrying in 5 seconds...");
                isFailed = true;
            }

            if (!isFailed) break;

            try { // wait for 5 seconds then try again
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private void populateHistoricalResponseData() {
        // populate latest response data
        boolean isFailed = false;
        while (true) {
            try {
                log.info("Fetching and storing historical data....");
                HistoricalDataAPIResponseDTO apiResponseHistoricalData = frankfurterHTTPClient.fetchHistorical("IDR", "USD", "2024-01-01", "2024-01-05");
                frankfurterDataRepository.setHistoricalResponseData(apiResponseHistoricalData);
                log.info("Finished fetching and storing historical data");
            } catch (RestClientException e) {
                log.error("An error occurred while fetching historical data, retrying in 5 seconds...");
                isFailed = true;
            }

            if (!isFailed) break;


            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private void populateCurrenciesResponseData() {
        // populate latest response data
        boolean isFailed = false;
        while (true) {
            try {
                log.info("Fetching and storing currencies data....");
                Map<String, String> apiResponseCurrenciesData = frankfurterHTTPClient.fetchCurrencies();
                frankfurterDataRepository.setCurrencies(apiResponseCurrenciesData);
                log.info("Finished fetching and storing currencies data");
            } catch (RestClientException e) {
                log.error("An error occurred while fetching currencies data, retrying in 5 seconds...");
                isFailed = true;
            }

            if (!isFailed) break;

            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

}
