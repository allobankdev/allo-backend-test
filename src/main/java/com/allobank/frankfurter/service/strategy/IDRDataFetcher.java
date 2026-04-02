package com.allobank.frankfurter.service.strategy;

import com.allobank.frankfurter.model.DataResult;

public interface IDRDataFetcher {
    DataResult fetchData();
    String getResourceType();
}