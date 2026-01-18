package com.example.allobank.backend.test.takehometest.fetcher;

import java.util.List;

public interface DataFetcher {
    public String getResourceType();

    public List<Object> fetchData();
}
