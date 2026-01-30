package com.example.allobank.strategy;

import java.util.List;

public interface IDRDataFetcher {
    String getResourceType();
    List<?> fetchData();
}