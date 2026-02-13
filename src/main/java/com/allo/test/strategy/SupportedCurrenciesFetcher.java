package com.allo.test.strategy;

import com.allo.test.restclient.FrankfurterClient;
import com.allo.test.store.FrankfurterCacheService;
import com.allo.test.utils.SupportedCurrencyTransformer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Map;

@Slf4j
@Component
public class SupportedCurrenciesFetcher implements IDRDataFetcher {

    private static final String CACHE_KEY = "currencies";

    private final FrankfurterCacheService cacheService;
    private final FrankfurterClient frankfurterClient;

    public SupportedCurrenciesFetcher(FrankfurterCacheService cacheService,
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
            return SupportedCurrencyTransformer.transform(
                    cacheService.get(
                            CACHE_KEY,
                            Map.class
                    )
            );
        }

        log.info("Cache not exists on {}. perform API Calling", CACHE_KEY);
        Map<String, String> response = frankfurterClient.getSupportedCurrency();

        cacheService.put(CACHE_KEY, response);

        return SupportedCurrencyTransformer.transform(response);
    }
}
