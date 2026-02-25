package com.allobank.finance.service;

import com.allobank.finance.exception.ResourceNotFoundException;
import com.allobank.finance.model.FinanceDataResult;
import com.allobank.finance.store.FinanceDataStore;
import com.allobank.finance.strategy.IDRDataFetcher;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Service layer yang mengorkestrasikan pengambilan data menggunakan strategi
 * dan menyajikannya dari in-memory store.
 */
@Slf4j
@Service
public class FinanceDataService {

    private final FinanceDataStore dataStore;
    private final Map<String, IDRDataFetcher> fetcherMap;

    /**
     * Spring secara otomatis menginjeksikan semua bean yang mengimplementasikan
     * {@link IDRDataFetcher} ke dalam Map dengan key = nama bean (@Component
     * value).
     * Ini adalah cara Spring mendukung map-based strategy lookup.
     */
    public FinanceDataService(FinanceDataStore dataStore,
            Map<String, IDRDataFetcher> fetcherMap) {
        this.dataStore = dataStore;
        this.fetcherMap = fetcherMap;
        log.info("FinanceDataService diinisialisasi dengan {} strategi: {}",
                fetcherMap.size(), fetcherMap.keySet());
    }

    /**
     * Memuat semua data dari Frankfurter API menggunakan setiap strategi yang
     * tersedia.
     * Dipanggil sekali saat startup oleh
     * {@link com.allobank.finance.runner.DataIngestionRunner}.
     *
     * @param webClient WebClient yang sudah dikonfigurasi
     */
    public void loadAll(WebClient webClient) {
        log.info("Mulai memuat data untuk semua {} resource types...", fetcherMap.size());

        Map<String, List<FinanceDataResult>> allData = new HashMap<>();

        fetcherMap.forEach((resourceType, fetcher) -> {
            log.info("Mengambil data untuk resource type: '{}'", resourceType);
            List<FinanceDataResult> results = fetcher.fetch(webClient);
            allData.put(resourceType, results);
            log.info("Berhasil memuat data untuk '{}': {} item", resourceType, results.size());
        });

        dataStore.initialize(allData);
        log.info("Semua data berhasil dimuat ke FinanceDataStore.");
    }

    /**
     * Mengambil data dari in-memory store berdasarkan resource type.
     * Controller memanggil method ini — tidak ada akses langsung ke eksternal API.
     *
     * @param resourceType jenis resource yang diminta
     * @return daftar hasil data
     * @throws ResourceNotFoundException jika resource type tidak dikenal
     */
    public List<FinanceDataResult> getData(String resourceType) {
        return dataStore.getByResourceType(resourceType)
                .orElseThrow(() -> new ResourceNotFoundException(resourceType));
    }
}
