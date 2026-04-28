package com.finance.aggregator.runner;

import com.finance.aggregator.service.DataStoreService;
import com.finance.aggregator.strategy.DataFetcherStrategy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class DataLoader implements ApplicationRunner {

    private final List<DataFetcherStrategy> strategies;
    private final DataStoreService dataStoreService;

    @Override
    public void run(ApplicationArguments args) {
        log.info("========================================");
        log.info("MEMULAI LOADING DATA DARI EXTERNAL API");
        log.info("========================================\n");

        long startTime = System.currentTimeMillis();

        List<Mono<Void>> loadingTasks = strategies.stream()
                .map(strategy -> strategy.fetch()
                        .doOnNext(data -> {
                            log.info("✓ Berhasil mengambil data: {}", strategy.getType());
                            dataStoreService.simpanData(strategy.getType(), data);
                        })
                        .doOnError(error -> {
                            log.error("✗ Gagal mengambil data {}: {}", strategy.getType(), error.getMessage());
                        })
                        .onErrorResume(error -> Mono.empty())
                        .then()
                )
                .toList();

        Mono.when(loadingTasks).block();

        long duration = System.currentTimeMillis() - startTime;

        log.info("\n========================================");
        if (dataStoreService.isDataLengkap()) {
            log.info("SEMUA DATA BERHASIL DIMUAT DARI EXTERNAL API!");
            log.info("Waktu: {} ms", duration);
            log.info("Resources: {}", dataStoreService.getAllData().keySet());
        } else {
            log.warn("HANYA {}/3 DATA YANG BERHASIL DIMUAT", dataStoreService.getAllData().size());
            log.warn("Gagal memuat: {}", getFailedResources());
        }
        log.info("========================================\n");
    }

    private String getFailedResources() {
        List<String> allTypes = List.of("latest_idr_rates", "historical_idr_usd", "supported_currencies");
        List<String> loaded = dataStoreService.getAllData().keySet().stream().toList();
        List<String> failed = allTypes.stream()
                .filter(type -> !loaded.contains(type))
                .toList();
        return failed.toString();
    }
}