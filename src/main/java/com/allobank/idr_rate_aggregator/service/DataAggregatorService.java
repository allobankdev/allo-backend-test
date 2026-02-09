package com.allobank.idr_rate_aggregator.service;

import com.allobank.idr_rate_aggregator.model.ResourceType;
import com.allobank.idr_rate_aggregator.service.strategy.IDRDataFetcherStrategy;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.EnumMap;
import java.util.Map;

/**
 * Service utama untuk:
 * 1. Mengambil semua data dari strategy saat startup
 * 2. Menyimpan data ke in-memory store
 * 3. Menyediakan data ke controller TANPA call ulang external API
 *
 * Constraint C:
 * - Data hanya di-load sekali
 * - Harus thread-safe
 * - Data immutable setelah load selesai
 */
@Service
public class DataAggregatorService {

    /**
     * Map strategy hasil dari StrategyConfig
     * Key : ResourceType
     * Value : Strategy implementation
     */
    private final Map<ResourceType, IDRDataFetcherStrategy> strategyMap;

    /**
     * In-memory store (thread-safe & immutable setelah load)
     * Menggunakan volatile agar visible antar thread
     */
    private volatile Map<ResourceType, Object> dataStore = Collections.emptyMap();

    public DataAggregatorService(Map<ResourceType, IDRDataFetcherStrategy> strategyMap) {
        this.strategyMap = strategyMap;
    }

    /**
     * Method ini dipanggil oleh DataLoaderRunner saat startup.
     * Akan:
     * - Loop semua strategy
     * - Fetch data dari external API
     * - Simpan ke immutable Map
     */
    public synchronized void loadAllData() {

        // Gunakan EnumMap karena key adalah enum (lebih efisien)
        Map<ResourceType, Object> tempStore = new EnumMap<>(ResourceType.class);

        for (Map.Entry<ResourceType, IDRDataFetcherStrategy> entry : strategyMap.entrySet()) {

            ResourceType type = entry.getKey();
            IDRDataFetcherStrategy strategy = entry.getValue();

            Object result = strategy.fetchData();
            tempStore.put(type, result);
        }

        // Bungkus dengan unmodifiableMap agar immutable
        this.dataStore = Collections.unmodifiableMap(tempStore);
    }

    /**
     * Mengambil data dari in-memory store
     * Tidak melakukan call ke external API
     */
    public Object getData(ResourceType resourceType) {
        
        if (dataStore.isEmpty()) {
            throw new IllegalStateException("Data belum di-load saat startup");
        }

        Object data = dataStore.get(resourceType);

        if (data == null) {
            throw new IllegalArgumentException(
                    "Data untuk resource type " + resourceType + " tidak ditemukan");
        }

        // Unified JSON Array
        return Collections.singletonList(data);
    }

}
