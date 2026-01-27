package com.interview.backend.services;

import com.interview.backend.utils.SpreadFactorUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

@Slf4j
@Service
public class FinanceDataStore {

    private static final int SPREAD_SCALE = 8;

    private final ReadWriteLock lock = new ReentrantReadWriteLock();
    private Map<String, Map<String, Object>> dataStore = new HashMap<>();
    private volatile boolean initialized = false;

    public void storeData(String resourceType, Map<String, Object> data) {
        lock.writeLock().lock();
        try {
            if (initialized) {
                return;
            }

            // Store an unmodifiable copy to ensure immutability
            dataStore.put(resourceType, Collections.unmodifiableMap(new HashMap<>(data)));

        } finally {
            lock.writeLock().unlock();
        }
    }

    public void markAsInitialized() {
        lock.writeLock().lock();
        try {
            dataStore = Collections.unmodifiableMap(new HashMap<>(dataStore));
            initialized = true;
        } finally {
            lock.writeLock().unlock();
        }
    }

    public Map<String, Object> getData(String resourceType) {
        lock.readLock().lock();
        try {
            return dataStore.get(resourceType);
        } finally {
            lock.readLock().unlock();
        }
    }

    public Map<String, Object> getData(String resourceType, String githubUsername) {
        lock.readLock().lock();
        try {
            Map<String, Object> baseData = dataStore.get(resourceType);

            if (baseData == null) {
                return null;
            }

            if ("latest_idr_rates".equals(resourceType) && githubUsername != null) {
                Map<String, Object> dynamicData = new HashMap<>(baseData);

                @SuppressWarnings("unchecked")
                Map<String, BigDecimal> rates = (Map<String, BigDecimal>) baseData.get("rates");
                if (rates != null && rates.containsKey("USD")) {
                    BigDecimal usdRate = rates.get("USD");
                    double spreadFactor = SpreadFactorUtil.calculateSpreadFactor(githubUsername);

                    BigDecimal buySpread = BigDecimal.ONE.divide(usdRate, SPREAD_SCALE, RoundingMode.HALF_UP)
                            .multiply(BigDecimal.valueOf(1 + spreadFactor))
                            .setScale(SPREAD_SCALE, RoundingMode.HALF_UP);

                    dynamicData.put("USD_BuySpread_IDR", buySpread);
                    dynamicData.put("spread_factor", spreadFactor);
                    dynamicData.put("github_username", githubUsername);

                }

                return Collections.unmodifiableMap(dynamicData);
            }

            return baseData;
        } finally {
            lock.readLock().unlock();
        }
    }

    public boolean isInitialized() {
        return initialized;
    }

    public java.util.Set<String> getAvailableResourceTypes() {
        lock.readLock().lock();
        try {
            return dataStore.keySet();
        } finally {
            lock.readLock().unlock();
        }
    }
}
