package com.allobanktest.idr.runner;

import com.allobanktest.idr.store.DataStore;
import com.allobanktest.idr.strategy.IDRDataFetcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple2;
import reactor.util.function.Tuples;

import java.time.Duration;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class StartupDataRunner implements ApplicationRunner {
    private static final Logger log = LoggerFactory.getLogger(StartupDataRunner.class);

    private final Map<String, IDRDataFetcher> fetchers;
    private final DataStore dataStore;

    public StartupDataRunner(Map<String, IDRDataFetcher> fetchers, DataStore dataStore) {
        this.fetchers = fetchers;
        this.dataStore = dataStore;
    }

    @Override
    public void run(ApplicationArguments args) {
        log.info("StartupDataRunner: fetching {} resources", fetchers.size());

        List<Mono<Tuple2<String, Map<String, Object>>>> monos = fetchers.entrySet().stream()
                .map(entry -> entry.getValue().fetchData()
                        .map(payload -> Tuples.of(entry.getKey(), payload))
                        .onErrorResume(ex -> Mono.just(Tuples.of(entry.getKey(), Map.of("error", ex.getMessage()))))
                )
                .collect(Collectors.toList());

        List<Tuple2<String, Map<String, Object>>> tuples = Flux.merge(monos)
                .collectList()
                .block(Duration.ofSeconds(30));

        if (tuples == null) {
            throw new IllegalStateException("Failed to load initial data");
        }

        Map<String, Map<String, Object>> map = tuples.stream()
                .collect(Collectors.toMap(Tuple2::getT1, Tuple2::getT2));

        dataStore.loadInitialData(map);
        log.info("StartupDataRunner: loaded {} resources", map.size());
    }
}
