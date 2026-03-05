package com.allobank.strategy;

import java.util.Map;

public interface IDRDataFetcher {
    
    Object fetchData();
    
    default Object fetchData(Map<String, String> params) {
        return fetchData(); 
    }
    
    String getResourceType();
}
