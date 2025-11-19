package com.allobank.service;

import com.allobank.client.FrankfurterClient;
import com.allobank.model.Cache;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class DataPreloader {

    private static final Logger log = LoggerFactory.getLogger(DataPreloader.class);

    private final FrankfurterClient client;
    private final Cache cache;
    private final double usdSpread;

    public DataPreloader(FrankfurterClient client, Cache cache,
                         @Value("${app.spread.usd-buy-idr:177.0}") double usdSpread) {
        this.client = client;
        this.cache = cache;
        this.usdSpread = usdSpread;
    }

    @PostConstruct
    public void preloadData() {
        log.info("Starting data preload from Frankfurter API...");
        try {
            Map<String, Object> latest = (Map<String, Object>) client.getLatestRates();
            @SuppressWarnings("unchecked")
            Map<String, Double> rates = (Map<String, Double>) latest.get("rates");
            rates.put("USD", rates.get("USD") + usdSpread);

            cache.put("latest", latest);
            cache.put("historical", client.getHistoricalRates());
            cache.put("currencies", client.getSupportedCurrencies());

            log.info("Data preload completed successfully | USD buy spread applied: +{} IDR", usdSpread);

        } catch (Exception e) {
            log.error("Failed to preload data at application startup", e);
        }
    }
}