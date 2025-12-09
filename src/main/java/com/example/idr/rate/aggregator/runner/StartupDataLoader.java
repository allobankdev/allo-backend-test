package com.example.idr.rate.aggregator.runner;

import com.example.idr.rate.aggregator.fetcher.IdrDataFetcher;
import com.example.idr.rate.aggregator.store.ImmutableDataStore;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.*;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Component
public class StartupDataLoader implements ApplicationRunner {

    private final Map<String, IdrDataFetcher> fetchers;
    private final ImmutableDataStore store;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        ExecutorService ex = Executors.newFixedThreadPool(Math.max(2, fetchers.size()));
        try {
            List<Callable<Map.Entry<String, Object>>> tasks = fetchers.entrySet().stream()
                    .map(e -> (Callable<Map.Entry<String, Object>>) () -> {
                        Object result = e.getValue().fetch().block();
                        return new AbstractMap.SimpleEntry<>(e.getKey(), result);
                    }).collect(Collectors.toList());

            List<Future<Map.Entry<String, Object>>> futures = ex.invokeAll(tasks, 30, TimeUnit.SECONDS);

            Map<String, Object> results = new HashMap<>();
            for (Future<Map.Entry<String, Object>> f : futures) {
                if (f.isDone()) {
                    Map.Entry<String, Object> entry = f.get();
                    results.put(entry.getKey(), entry.getValue());
                } else {
                    throw new IllegalStateException("One of the fetch tasks did not finish in time.");
                }
            }
            store.setDataIfEmpty(results);
        } finally {
            ex.shutdown();
        }
    }
}
