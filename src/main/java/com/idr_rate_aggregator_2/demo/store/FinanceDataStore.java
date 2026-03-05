package com.idr_rate_aggregator_2.demo.store;

import com.idr_rate_aggregator_2.demo.dto.CurrencyResponse;
import com.idr_rate_aggregator_2.demo.dto.HistoricalRate;
import com.idr_rate_aggregator_2.demo.dto.LatestRatesResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Thread-safe in-memory data store untuk menyimpan data yang di-fetch saat startup.
 * Data bersifat immutable setelah di-load (hanya bisa dibaca, tidak bisa dimodifikasi).
 */
@Slf4j
@Service
public class FinanceDataStore {

    // Menggunakan ConcurrentHashMap untuk thread-safety
    private final Map<String, Object> dataStore = new ConcurrentHashMap<>();

    // Flag untuk menandai apakah data sudah siap
    private volatile boolean initialized = false;

    /**
     * Menyimpan data ke dalam store (hanya dipanggil saat startup)
     */
    public void storeData(String resourceType, Object data) {
        if (initialized) {
            log.warn("Attempting to store data after initialization - ignoring for resource: {}", resourceType);
            return;
        }

        // Buat defensive copy untuk memastikan immutability
        Object immutableData = createImmutableCopy(data);
        dataStore.put(resourceType, immutableData);
        log.info("Stored data for resource type: {}", resourceType);
    }

    /**
     * Mendapatkan data dari store (untuk endpoint API)
     */
    public Object getData(String resourceType) {
        Object data = dataStore.get(resourceType);
        System.out.println("data testoing" + data.toString());
        if (data == null) {
            throw new IllegalStateException("Data not available for resource type: " + resourceType);
        }
        return data;
    }

    /**
     * Mendapatkan data dengan tipe spesifik (type-safe)
     */
    @SuppressWarnings("unchecked")
    public LatestRatesResponse getLatestRates() {
        return (LatestRatesResponse) getData("latest_idr_rates");
    }

    @SuppressWarnings("unchecked")
    public List<HistoricalRate> getHistoricalRates() {
        return (List<HistoricalRate>) getData("historical_idr_usd");
    }

    @SuppressWarnings("unchecked")
    public List<CurrencyResponse> getSupportedCurrencies() {
        return (List<CurrencyResponse>) getData("supported_currencies");
    }

    /**
     * Cek apakah data untuk resource type tertentu tersedia
     */
    public boolean hasData(String resourceType) {
        return dataStore.containsKey(resourceType);
    }

    /**
     * Mendapatkan semua data yang tersedia (immutable view)
     */
    public Map<String, Object> getAllData() {
        return Collections.unmodifiableMap(dataStore);
    }

    /**
     * Menandai bahwa inisialisasi selesai
     */
    public void markInitialized() {
        this.initialized = true;
        log.info("FinanceDataStore initialization completed. Data available for: {}", dataStore.keySet());
    }

    /**
     * Cek apakah store sudah siap digunakan
     */
    public boolean isInitialized() {
        return initialized;
    }

    /**
     * Membuat immutable copy dari data untuk mencegah modifikasi
     */
    private Object createImmutableCopy(Object data) {
        if (data instanceof List) {
            // Buat immutable list
            return Collections.unmodifiableList((List<?>) data);
        } else if (data instanceof Map) {
            // Buat immutable map
            return Collections.unmodifiableMap((Map<?, ?>) data);
        } else {
            // Untuk object biasa (seperti LatestRatesResponse),
            // kita return as-is karena tidak bisa diubah tanpa setter
            // Tapi pastikan class-nya dirancang immutable
            return data;
        }
    }
}