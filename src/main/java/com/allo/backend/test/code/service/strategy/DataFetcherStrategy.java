package com.allo.backend.test.code.service.strategy;

import org.springframework.web.reactive.function.client.WebClient;

/**
 * Strategy interface for fetching different types of financial data.
 * Each implementation handles a specific resource type.
 */
public interface DataFetcherStrategy {

    /**
     * Returns the resource type identifier that this strategy handles.
     */
    String getResourceType();

    /**
     * Fetches data from the external API and transforms it into domain model.
     * @param webClient The WebClient configured for the external API
     * @return The transformed data as domain object
     */
    Object fetchData(WebClient webClient);
}
