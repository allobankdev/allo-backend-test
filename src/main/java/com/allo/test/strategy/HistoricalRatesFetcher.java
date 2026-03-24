package com.allo.test.strategy;

import com.allo.test.service.ExternalApiService;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public class HistoricalRatesFetcher implements  IDRDataFetcher {
    private final ExternalApiService externalApiService;
    public HistoricalRatesFetcher(ExternalApiService externalApiService) {
        this.externalApiService = externalApiService;
    }
    @Override
    public String getResourceType() {
        return "historical_idr_usd";
    }

    @Override
    public List<Object> fetchData() {

        try {
            Map<String, Object> data =
                    externalApiService.getHistoricalRatesParsed();

            return List.of(data);
        } catch (Exception e) {
            return List.of(Map.of("error", "failed to fetch historical data"));
        }
    }
}
