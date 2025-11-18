package com.allo.test.modules.finance.client.strategy;

import org.springframework.web.reactive.function.client.WebClient;

public interface FrankfurterResourceStrategy<T> {

    /**
     * Fetches data from a specific Frankfurter API endpoint.
     *
     * @param webClient the WebClient instance to use for the HTTP request
     * @return the response data of type T
     */
    T fetchData(WebClient webClient);

    /**
     * Returns the name/identifier of this strategy.
     *
     * @return strategy name
     */
    String getStrategyName();
}
