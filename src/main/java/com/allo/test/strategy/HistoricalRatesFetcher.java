package com.allo.test.strategy;

import java.util.List;

public class HistoricalRatesFetcher implements  IDRDataFetcher {
    @Override
    public String getResourceType() {
        return "hictorical_idr_usd";
    }

    @Override
    public List<Object> fetchData() {
        return List.of("hictoricaal dummy");
    }
}
