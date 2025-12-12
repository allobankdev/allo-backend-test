package com.allobank.finance.strategy;

public interface IDRDataFetcher {

    /**
     * Fetches data from Frankfurter API for a specific resource type
     * @return the fetched and transformed data
     */
    Object fetchData();
}
