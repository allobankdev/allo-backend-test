package com.allo.test.strategy;

import java.util.List;

public class CurrencyFetcher implements  IDRDataFetcher{
    @Override
    public String getResourceType() {
        return "supported_currencies";
    }

    @Override
    public List<Object> fetchData() {
        return List.of( "currency dummy");
    }
}
