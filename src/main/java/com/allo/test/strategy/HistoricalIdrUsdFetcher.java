package com.allo.test.strategy;

import com.allo.test.dto.response.HistoricalIdrUsdResponse;
import com.allo.test.restclient.FrankfurterClient;
import com.allo.test.store.FrankfurterCacheService;
import com.allo.test.utils.HistoricalRateTransformer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class HistoricalIdrUsdFetcher implements IDRDataFetcher {

    private static final String CACHE_KEY = "historical_idr_usd";

    private final FrankfurterCacheService cacheService;
    private final FrankfurterClient frankfurterClient;

    public HistoricalIdrUsdFetcher(FrankfurterCacheService cacheService,
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
            log.info("Cache exists on {}, return from cache", CACHE_KEY);
            return HistoricalRateTransformer.transform(
                    cacheService.get(
                            CACHE_KEY,
                            HistoricalIdrUsdResponse.class
                    )
            );
        }

        log.info("Cache not exists on {}. Perform API calling", CACHE_KEY);

        HistoricalIdrUsdResponse response = frankfurterClient.getHistorical();

        cacheService.put(CACHE_KEY, response);

        return HistoricalRateTransformer.transform(response);
    }
}
