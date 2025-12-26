package com.allobank.finance.service.strategy;

// import java.util.*;

public interface IDRDataFetcher {
    String getResourceType();
    // List<Object> fetchData();
    Object fetch();    
} 
