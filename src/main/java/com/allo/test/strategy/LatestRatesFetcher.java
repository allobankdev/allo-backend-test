package com.allo.test.strategy;

import com.allo.test.dto.LatestRatesResponse;
import com.allo.test.dto.LatestRatesResult;
import com.allo.test.service.ExternalApiService;
import com.allo.test.service.SpreadCalculator;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public class LatestRatesFetcher implements IDRDataFetcher {
    private static final String USERNAME = "salwafadillah171011450139";
    private final ExternalApiService externalApiService;

    public LatestRatesFetcher(ExternalApiService externalApiService) {
        this.externalApiService = externalApiService;
    }
    @Override
    public String getResourceType() {
        return "latest_idr_rates";
    }

    @Override
    public List<Object> fetchData() {

        LatestRatesResponse data = externalApiService.getLatestRatesParsed();

        Double usdRate = data.getRates().get("USD");

        double spreadFactor = SpreadCalculator.calculateSpreadFactor(USERNAME);

        double result = (1 / usdRate) * (1 + spreadFactor);

        LatestRatesResult response =
                new LatestRatesResult(usdRate, spreadFactor, result);

        return List.of(response);
    }
}
