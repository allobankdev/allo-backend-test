package com.allo.idr.runner;

import com.allo.idr.cache.ImmutableDataCache;
import com.allo.idr.enums.ResourceType;
import com.allo.idr.exception.ExternalApiException;
import com.allo.idr.service.FetcherStrategyRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class InitialDataLoader implements ApplicationRunner {
    private final Logger log = LoggerFactory.getLogger(InitialDataLoader.class);
    private final FetcherStrategyRegistry fetch;
    private final ImmutableDataCache cache;

    public InitialDataLoader(FetcherStrategyRegistry fetch, ImmutableDataCache cache) {
        this.fetch = fetch;
        this.cache = cache;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        Map<ResourceType, List<?>> temp = new HashMap<>();

        for (ResourceType type : fetch.getAllResourcesType()){
            fetch.getStrategy(type).ifPresent(fetcher -> {
                int attmp = 0;
                final int maxAtmpt = 3;
                final long baseBackoffMs = 1000L;
                while (attmp < maxAtmpt) {
                    attmp++;
                    try {
                        log.info("Preloading {} (attempt {}/{}", type, attmp, maxAtmpt);
                        var data = fetcher.fetcData();
                        temp.put(type, data);
                        log.info("Preloading {} entries: {}", type, data == null ? 0 : data.size());
                        break;
                    } catch (ExternalApiException e){
                        log.warn("Error preloading {}: {}", type, e.getMessage());
                        if (attmp >= maxAtmpt) {
                            log.error("Failed to preload {} after {} attemp", type, attmp);
                            throw new RuntimeException("Failed to preload resource" + type, e);
                        }
                        try {
                            Thread.sleep(baseBackoffMs * attmp);
                        } catch (InterruptedException ignored){

                        }
                    }
                }
            });
        }
        cache.populate(temp);
        log.info("Cache published with {} resource", cache.getAll().size());
    }
}
