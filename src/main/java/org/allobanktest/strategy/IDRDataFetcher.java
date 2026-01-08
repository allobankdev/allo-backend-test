package org.allobanktest.strategy;

import org.allobanktest.store.FinancialDataStore;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;

public interface IDRDataFetcher {
    String resourceKey();

    List<?> load(WebClient webClient, String githubUsername);

    List<?> getCached(FinancialDataStore store);
}
