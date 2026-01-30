package com.example.demo.strategy;

import java.util.List;

public interface IDRDataFetcher {
    String getType();
    List<?> fetchData();
}
