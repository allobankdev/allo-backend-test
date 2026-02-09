package com.allobank.idr_rate_aggregator.runner;

import com.allobank.idr_rate_aggregator.service.DataAggregatorService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

/**
 * Runner yang akan dijalankan saat aplikasi startup.
 *
 * Constraint C:
 * - Semua data dari external API harus di-fetch SEKALI saja saat startup
 * - Data disimpan ke in-memory store
 * - Jika gagal load, aplikasi sebaiknya tidak lanjut berjalan
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class DataLoaderRunner implements ApplicationRunner {

    private final DataAggregatorService dataAggregatorService;

    /**
     * Method ini otomatis dipanggil Spring setelah context selesai dibuat
     */
    @Override
    public void run(ApplicationArguments args) {

        log.info("Memulai proses inisialisasi data dari Frankfurter API...");

        try {

            // Load semua data dari strategy
            dataAggregatorService.loadAllData();

            log.info("Semua data berhasil di-load ke in-memory store.");

        } catch (Exception e) {

            log.error("Gagal memuat data saat startup. Aplikasi akan dihentikan.", e);

            // Fail fast → hentikan aplikasi jika data tidak bisa di-load
            throw new IllegalStateException("Startup gagal karena data tidak bisa dimuat", e);
        }
    }
}