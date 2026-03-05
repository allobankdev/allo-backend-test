package com.idr_rate_aggregator_2.demo.store;

import com.idr_rate_aggregator_2.demo.Error.ExternalApiException;
import com.idr_rate_aggregator_2.demo.dto.CurrencyResponse;
import com.idr_rate_aggregator_2.demo.dto.HistoricalRate;
import com.idr_rate_aggregator_2.demo.dto.LatestRatesResponse;
import com.idr_rate_aggregator_2.demo.idr_data_fetchers_interface.IDRDataFetcher;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
@Component
public class DataInitializationRunner implements ApplicationRunner {

    private final FinanceDataStore dataStore;
    private final List<IDRDataFetcher> dataFetchers;
    private final AtomicInteger successCount = new AtomicInteger(0);
    private final AtomicInteger failureCount = new AtomicInteger(0);

    @Autowired
    public DataInitializationRunner(FinanceDataStore dataStore, List<IDRDataFetcher> dataFetchers) {
        this.dataStore = dataStore;
        this.dataFetchers = dataFetchers;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        log.info("==================================================");
        log.info("🚀 Starting application data initialization...");
        log.info("==================================================");

        long startTime = System.currentTimeMillis();

        try {
            // Reset counters
            successCount.set(0);
            failureCount.set(0);

            // Fetch semua data secara parallel dengan timeout
            CompletableFuture<Void> fetchFuture = fetchAllDataParallel();

            // Tunggu sampai semua selesai dengan timeout 30 detik
            fetchFuture.get(30, TimeUnit.SECONDS);

            // Tandai bahwa inisialisasi selesai
            dataStore.markInitialized();

            long duration = System.currentTimeMillis() - startTime;
            log.info("==================================================");
            log.info("✅ Data initialization completed in {} ms", duration);
            log.info("📊 Summary: {} successful, {} failed", successCount.get(), failureCount.get());
            log.info("📦 Available data: {}", dataStore.getAllData().keySet());
            log.info("==================================================");

            // Log detail setiap resource
            logDataSummary();

            // Jika ada yang gagal, tampilkan warning
            if (failureCount.get() > 0) {
                log.warn("⚠️ Some data failed to load. Application running with partial data.");
            }

        } catch (Exception e) {
            log.error("❌ Failed to initialize application data: {}", e.getMessage(), e);
            log.warn("⚠️ Application starting with incomplete data. Some endpoints may return 503.");
        }
    }

    private CompletableFuture<Void> fetchAllDataParallel() {
        return Flux.fromIterable(dataFetchers)
                .flatMap(fetcher -> {
                    String resourceType = fetcher.getResourceType();
                    log.info("📥 Fetching data for: {}", resourceType);

                    return Mono.fromCallable(() -> fetcher.fetchData())
                            .flatMap(mono -> (Mono<?>) mono)
                            .timeout(Duration.ofSeconds(10))
                            .doOnSuccess(data -> {
                                if (data != null) {
                                    // Cek apakah data valid (tidak fallback/empty)
                                    if (isValidData(data)) {
                                        dataStore.storeData(resourceType, data);
                                        successCount.incrementAndGet();
                                        log.info("✅ Successfully fetched and stored: {}", resourceType);
                                    } else {
                                        failureCount.incrementAndGet();
                                        log.warn("⚠️ Received fallback/empty data for: {}", resourceType);
                                    }
                                } else {
                                    failureCount.incrementAndGet();
                                    log.warn("⚠️ Received null data for: {}", resourceType);
                                }
                            })
                            .doOnError(error -> {
                                failureCount.incrementAndGet();
                                if (error instanceof ExternalApiException) {
                                    ExternalApiException extEx = (ExternalApiException) error;
                                    log.error("❌ External API error for {}: Status={}, Message={}",
                                            resourceType, extEx.getStatusCode(), extEx.getMessage());
                                } else {
                                    log.error("❌ Failed to fetch {}: {}", resourceType, error.getMessage());
                                }
                            })
                            .onErrorResume(error -> {
                                // Return empty to continue with other fetchers
                                return Mono.empty();
                            });
                })
                .collectList()
                .toFuture()
                .thenApply(result -> null);
    }

    private boolean isValidData(Object data) {
        if (data == null) return false;

        // Cek untuk List (historical rates dan currencies)
        if (data instanceof List) {
            return !((List<?>) data).isEmpty();
        }

        // Cek untuk LatestRatesResponse
        if (data instanceof LatestRatesResponse) {
            LatestRatesResponse response = (LatestRatesResponse) data;
            return response.getRates() != null && !response.getRates().isEmpty();
        }

        return true;
    }

    private void logDataSummary() {
        if (dataStore.hasData("latest_idr_rates")) {
            LatestRatesResponse latest = dataStore.getLatestRates();
            log.info("📈 Latest IDR Rates: base={}, date={}, USD spread={}",
                    latest.getBase(),
                    latest.getDate(),
                    latest.getUSD_BuySpread_IDR() != null ? latest.getUSD_BuySpread_IDR() : "N/A");
        } else {
            log.warn("📈 Latest IDR Rates: NOT AVAILABLE");
        }

        if (dataStore.hasData("historical_idr_usd")) {
            List<HistoricalRate> historical = dataStore.getHistoricalRates();
            log.info("📉 Historical IDR-USD Rates: {} days ({} to {})",
                    historical.size(),
                    historical.isEmpty() ? "N/A" : historical.get(0).getDate(),
                    historical.isEmpty() ? "N/A" : historical.get(historical.size() - 1).getDate());
        } else {
            log.warn("📉 Historical IDR-USD Rates: NOT AVAILABLE");
        }

        if (dataStore.hasData("supported_currencies")) {
            List<CurrencyResponse> currencies = dataStore.getSupportedCurrencies();
            log.info("💱 Supported Currencies: {} currencies available", currencies.size());
        } else {
            log.warn("💱 Supported Currencies: NOT AVAILABLE");
        }
    }
}