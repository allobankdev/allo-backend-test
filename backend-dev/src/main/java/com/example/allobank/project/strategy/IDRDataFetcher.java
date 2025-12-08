package com.example.allobank.project.strategy;

public interface IDRDataFetcher {
    String getResourceType();  
    Object fetchData(String... params);
}
