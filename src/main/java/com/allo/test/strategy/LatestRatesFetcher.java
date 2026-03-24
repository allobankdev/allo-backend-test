package com.allo.test.strategy;

import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class LatestRatesFetcher implements IDRDataFetcher {
    @Override
    public String getResourceType() {
        return "latest_idr_rates";
    }

    @Override
    public List<Object> fetchData() {
        return List.of("latest dummy");
    }
}
