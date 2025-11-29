package com.allobank.backendtest.runner;

import com.allobank.backendtest.config.ExternalApiProperties;
import com.allobank.backendtest.fetcher.IDRDataFetcher;
import com.allobank.backendtest.service.ImmutableFinanceStore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.*;
import java.util.stream.Collectors;

/**
 * ApplicationRunner that fetches all data ONCE at startup and populates ImmutableFinanceStore.
 * Implements per-task retry/backoff and avoids application hang by respecting max retries.
 */
@Component
public class StartupDataLoader implements ApplicationRunner {
    private final List<IDRDataFetcher> fetchers;
    private final ImmutableFinanceStore store;
    private final ExternalApiProperties props;
    private static final Logger log = LoggerFactory.getLogger(StartupDataLoader.class);

    public StartupDataLoader(List<IDRDataFetcher> fetchers, ImmutableFinanceStore store, ExternalApiProperties props) {
        this.fetchers = fetchers;
        this.store = store;
        this.props = props;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        log.info("StartupDataLoader: begin fetching {} resources", fetchers.size());
        ExecutorService ex = Executors.newFixedThreadPool(Math.max(2, fetchers.size()));
        try {
            List<Callable<Map.Entry<String, List<?>>>> tasks = fetchers.stream().map(f ->
                    (Callable<Map.Entry<String, List<?>>>)() -> {
                        String key = f.resourceKey();
                        int attempts = 0;
                        while (true) {
                            attempts++;
                            try {
                                List<?> result = f.fetchSync();
                                log.info("Fetched resource '{}' ({} items)", key, result == null ? 0 : result.size());
                                return Map.entry(key, result == null ? Collections.emptyList() : result);
                            } catch (Exception exx) {
                                log.warn("Fetch failed for '{}', attempt {}/{}: {}", key, attempts, props.getMaxRetries(), exx.getMessage());
                                if (attempts >= props.getMaxRetries()) {
                                    log.error("Exhausted retries for '{}', storing empty placeholder", key);
                                    // fallback: store an error marker or empty list (we choose empty list to keep API stable)
                                    return Map.entry(key, Collections.emptyList());
                                }
                                try {
                                    Thread.sleep(props.getRetryBackoffMs() * attempts);
                                } catch (InterruptedException ie) {
                                    Thread.currentThread().interrupt();
                                    throw new IllegalStateException("Interrupted while retrying fetch for " + key, ie);
                                }
                            }
                        }
                    }
            ).collect(Collectors.toList());

            List<Future<Map.Entry<String, List<?>>>> futures = ex.invokeAll(tasks, 30, TimeUnit.SECONDS);

            Map<String, List<?>> collected = new HashMap<>();
            for (Future<Map.Entry<String, List<?>>> fut : futures) {
                try {
                    Map.Entry<String, List<?>> e = fut.get();
                    if (e != null) collected.put(e.getKey(), e.getValue());
                } catch (CancellationException ce) {
                    log.error("A fetch task was cancelled: {}", ce.getMessage());
                } catch (ExecutionException ee) {
                    log.error("Execution exception during fetch tasks: {}", ee.getMessage());
                }
            }

            // ensure all known keys exist (fill missing with empty list)
            for (IDRDataFetcher f : fetchers) {
                collected.putIfAbsent(f.resourceKey(), Collections.emptyList());
            }

            store.initialize(collected);
            log.info("StartupDataLoader: store initialized with keys {}", collected.keySet());
        } finally {
            ex.shutdownNow();
        }
    }
}
