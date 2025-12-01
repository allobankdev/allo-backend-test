package com.example.idraggregator.service.strategy;

public interface IDRDataFetcher<T> {
    /**
     * Fetch the resource from the external client and transform into DTO.
     * @return resource DTO (type depends on implementation)
     * @throws Exception when fetching fails
     */
    T fetch() throws Exception;

    /**
     * The resource key used to identify the result in the in-memory store.
     */
    String resourceKey();
}

