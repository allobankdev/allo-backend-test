package com.allobank.financeapi.runner;

import com.allobank.financeapi.model.enums.ResourceType;
import com.allobank.financeapi.service.FinanceDataService;
import com.allobank.financeapi.service.strategy.DataFetcherStrategy;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class DataInitializer implements ApplicationRunner {

    private static final Logger log = LoggerFactory.getLogger(DataInitializer.class);

    private final FinanceDataService financeDataService;
    private final List<DataFetcherStrategy> dataFetcherStrategies;

    public DataInitializer(FinanceDataService financeDataService, List<DataFetcherStrategy> dataFetcherStrategies) {
        this.financeDataService = financeDataService;
        this.dataFetcherStrategies = dataFetcherStrategies;
    }

    @Override
    public void run(ApplicationArguments args) {
        log.info("Starting data initialization...");

        Map<ResourceType, DataFetcherStrategy> strategyMap = this.dataFetcherStrategies.stream()
                .filter(strategy -> strategy.getResourceType() != null)
                .collect(Collectors.toMap(DataFetcherStrategy::getResourceType, Function.identity()));

        Flux.fromIterable(strategyMap.entrySet())
                .flatMap(entry -> entry.getValue().fetchData()
                        .doOnNext(data -> this.financeDataService.storeData(entry.getKey(), data))
                        .doOnError(error -> log.error("Error fetching data for resource {}: {}", entry.getKey(), error.getMessage()))
                        .onErrorResume(error -> Mono.empty())) // Continue with other strategies if one fails
                .doOnComplete(() -> {
                    this.financeDataService.setImmutable();
                    log.info("Data initialization completed.");
                })
                .subscribe();
    }
}
