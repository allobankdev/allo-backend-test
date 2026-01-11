package com.prasetyahs.allo.finance.strategy;

import com.prasetyahs.allo.finance.store.InMemoryDataStore;
import org.springframework.web.reactive.function.client.WebClient;

public interface IDRDataFetcher {
    String getResourceType();

    /**
     * Fetches data from external API and applies transformation logic.
     * 
     * @param client WebClient to use for requests
     * @return The processed data object
     */
    Object fetchAndProcess(WebClient client);

    /**
     * Retrieves the stored data for this strategy.
     * 
     * @param store The in-memory store
     * @return The cached data
     */
    default Object retrieveData(InMemoryDataStore store) {
        return store.retrieve(getResourceType());
    }
}
