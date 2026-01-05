package com.allobank.allobanktest.strategy;

public interface IDRDataFetcher {


    /**
     * Identifier of the resource handled by this strategy.
     */
    String getResourceType();

    /**
     * Fetches and transforms data from the external API.
     * Executed once during application startup.
     */
    Object fetchAndTransform();

}
