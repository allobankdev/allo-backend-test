package com.allo.test.strategy;

import com.allo.test.dto.LatestRatesResponse;
import com.allo.test.service.ExternalApiService;
import com.allo.test.service.SpreadCalculator;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public class LatestRatesFetcher implements IDRDataFetcher {
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

        try {
            LatestRatesResponse data =
                    externalApiService.getLatestRatesParsed();

            Double usdRate = data.getRates().get("USD");

            if (usdRate == null) {
                throw new RuntimeException("USD rate not found");
            }
            double spreadFactor =
                    SpreadCalculator.calculateSpreadFactor("yourgithubusername");

            double result = (1 / usdRate) * (1 + spreadFactor);

            return List.of(
                    Map.of(
                            "usdRate", usdRate,
                            "spreadFactor", spreadFactor,
                            "usdBuySpreadIdr", result
                    )
            );

        } catch (Exception e) {
            return List.of(Map.of("error", "failed to fetch latest rates"));
        }
    }
}
