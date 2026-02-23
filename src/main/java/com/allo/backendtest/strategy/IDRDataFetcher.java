package com.allo.backendtest.strategy;

import java.util.List;

public interface IDRDataFetcher {

    String getResourceType();

    List<Object> fetchAndTransform();
}
