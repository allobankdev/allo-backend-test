package com.allo.test.strategy;

import com.allo.test.dto.response.LatestIDRRatesResponse;
import com.allo.test.restclient.FrankfurterClient;
import com.allo.test.store.FrankfurterCacheService;
import com.allo.test.utils.SpreadCalculator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class LatestIdrRatesFetcher implements IDRDataFetcher {

    @Value("${github.username}")
    private String githubUsername;

    private static final String CACHE_KEY = "latest_idr_rates";

    private final FrankfurterCacheService cacheService;
    private final FrankfurterClient frankfurterClient;

    public LatestIdrRatesFetcher(FrankfurterCacheService cacheService,
                                 FrankfurterClient frankfurterClient) {
        this.cacheService = cacheService;
        this.frankfurterClient = frankfurterClient;
    }

    @Override
    public String getResourceType() {
        return CACHE_KEY;
    }

    @Override
    public Object fetchData() {

        if (cacheService.contains(CACHE_KEY)) {
            log.info("Cache exists on {} , return from cache", CACHE_KEY);

            return cacheService.get(CACHE_KEY, LatestIDRRatesResponse.class);
        }

        log.info("Cache not exists on {}. perform API Calling", CACHE_KEY);
        LatestIDRRatesResponse response = frankfurterClient.getLatestRates();
        response.setUSD_BuySpread_IDR(
                SpreadCalculator.calculateUsdBuySpreadIdr(
                        this.githubUsername,
                        response.getRates().get("USD")
                )
        );

        cacheService.put(CACHE_KEY, response);

        return response;
    }


}
