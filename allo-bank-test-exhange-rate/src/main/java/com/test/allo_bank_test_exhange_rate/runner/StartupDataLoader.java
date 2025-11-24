package com.test.allo_bank_test_exhange_rate.runner;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import org.springframework.boot.ApplicationArguments;

import com.test.allo_bank_test_exhange_rate.enums.ResourceType;
import com.test.allo_bank_test_exhange_rate.service.DataFetcherFactory;
import com.test.allo_bank_test_exhange_rate.service.IDRDataFetcher;
import com.test.allo_bank_test_exhange_rate.store.ImmutableDataStore;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class StartupDataLoader implements ApplicationRunner {
    
    private final ImmutableDataStore store;
    private final String githubUsername;
    private final DataFetcherFactory dataFetcherFactory;

    public StartupDataLoader(ImmutableDataStore store, @Value("${app.github.username}") String githubUsername, DataFetcherFactory dataFetcherFactory) {
        this.store = store;
        this.githubUsername = githubUsername;
        this.dataFetcherFactory = dataFetcherFactory;
    }

    public void run(ApplicationArguments args) throws Exception {
        Map<String, Object> aggregated = new HashMap<>();
        Arrays.asList(ResourceType.values()).forEach(
            type -> {
                log.info("fetch data from resource : {}", type.toString());
                IDRDataFetcher fetcher = dataFetcherFactory.get(type.toString());
                try {
                    var method = fetcher.getClass().getMethod("setGithubUsername", String.class);
                    method.invoke(fetcher, githubUsername);
                } catch (NoSuchMethodException ignore) {
                } catch (Exception e) {
                    // ignore
                }

                try {
                    Object data = fetcher.fetchData().block();
                    aggregated.put(type.toString(), data);
                } catch (Exception ex) {
                    Map<String,Object> err = Map.of("error", "failed to fetch: " + ex.getMessage());
                    aggregated.put(type.toString(), err);
                }
            }
        );
        store.loadInitialData(aggregated);
    }
}
