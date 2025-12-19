package com.zultest.allobank_backend_test.service;

import java.util.List;

public interface IDRDataFetcherInterface {

    String resourceType();
    List<?> fetchData();
}
