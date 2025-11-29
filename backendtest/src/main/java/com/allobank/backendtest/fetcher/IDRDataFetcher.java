package com.allobank.backendtest.fetcher;

import java.util.List;

public interface IDRDataFetcher {
    /**
     * Logical resource key (should match bean name)
     */
    String resourceKey();

    /**
     * Synchronous fetch used by startup loader.
     * Implementations should throw exceptions on fatal errors.
     */
    List<?> fetchSync() throws Exception;
}
