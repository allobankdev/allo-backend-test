package com.example.idr.service.strategy;

import java.util.List;

public interface IDRDataFetcher {

    /**
     * Will be used as resource path in Frankfurter API
     * exmpl: latest_idr_rates
     */
    String getResourceType();

    /**
     * Fetch + transform data from Frankfurter API
     * Called ONCE at startup
     */
    List<?> fetchAndTransform();
}
