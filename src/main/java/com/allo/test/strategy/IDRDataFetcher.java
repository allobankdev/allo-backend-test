package com.allo.test.strategy;

import java.util.List;

public interface IDRDataFetcher {
    String getResourceType();
    List<Object> fetchData();
}
