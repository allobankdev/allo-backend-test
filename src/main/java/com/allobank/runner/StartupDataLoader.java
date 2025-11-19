package com.allobank.runner;

import com.allobank.service.IDRDataFetcher;
import com.allobank.service.InMemoryDataStore;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class StartupDataLoader implements ApplicationRunner {

    private final Map<String, IDRDataFetcher<?>> fetcherStrategies;
    private final InMemoryDataStore dataStore;

    @Override
    public void run(ApplicationArguments args) {

        List<Mono<Void>> tasks = fetcherStrategies.entrySet().stream()
                .map(entry ->
                        entry.getValue()
                                .fetch()
                                .doOnNext(result -> dataStore.put(entry.getKey(), result))
                                .then()
                )
                .toList();

        Mono.when(tasks)
                .doOnSuccess(v -> dataStore.makeImmutable())
                .block();
    }
}
