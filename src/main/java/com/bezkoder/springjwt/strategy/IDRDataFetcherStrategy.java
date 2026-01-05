package com.bezkoder.springjwt.strategy;

import java.util.List;

public interface IDRDataFetcherStrategy {

    /**
     * Must match {resourceType} from GET /api/finance/data/{resourceType}
     */
    String resourceType();

    /**
     * Called once during startup to fetch + transform data.
     * Implementations must NOT rely on the in-memory store here,
     * because the store is finalized only after all strategies finish loading.
     */
    void loadAtStartup();

    /**
     * Returns the immutable data produced by loadAtStartup().
     */
    List<Object> loadedData();

    /**
     * Called by controller at runtime; must NOT call external API.
     * Must serve from in-memory store.
     */
    List<Object> getData();
}
