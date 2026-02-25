package com.allobank.finance.runner;

import com.allobank.finance.service.FinanceDataService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

/**
 * Startup runner yang memuat semua data dari Frankfurter API tepat satu kali
 * saat aplikasi pertama kali dimulai, sebelum menerima request HTTP.
 *
 * <p>
 * <b>Mengapa ApplicationRunner (Constraint C)?</b>
 * <ul>
 * <li>ApplicationRunner dieksekusi setelah Spring context sepenuhnya
 * ter-refresh
 * dan semua bean siap digunakan, memastikan dependensi seperti WebClient
 * sudah tersedia dan sudah dikonfigurasi.</li>
 * <li>Berbeda dengan {@code @PostConstruct} yang dipanggil selama fase
 * inisialisasi bean
 * (sebelum context refresh selesai penuh), ApplicationRunner menjamin urutan
 * eksekusi
 * yang tepat dan lebih mudah di-test secara integrasi.</li>
 * <li>Spring Boot menjamin ApplicationRunner selesai sebelum aplikasi mulai
 * menerima
 * traffic produksi, sehingga endpoint tidak pernah melayani data kosong.</li>
 * </ul>
 */
@Slf4j
@Component
public class DataIngestionRunner implements ApplicationRunner {

    private final FinanceDataService financeDataService;
    private final WebClient webClient;

    public DataIngestionRunner(FinanceDataService financeDataService,
            @Qualifier("frankfurterWebClient") WebClient webClient) {
        this.financeDataService = financeDataService;
        this.webClient = webClient;
    }

    @Override
    public void run(ApplicationArguments args) {
        log.info("=== DataIngestionRunner: Memulai inisialisasi data dari Frankfurter API ===");
        long startTime = System.currentTimeMillis();

        try {
            financeDataService.loadAll(webClient);
            long elapsed = System.currentTimeMillis() - startTime;
            log.info("=== DataIngestionRunner: Data berhasil dimuat dalam {} ms ===", elapsed);
        } catch (Exception e) {
            log.error("=== DataIngestionRunner: GAGAL memuat data! Aplikasi mungkin tidak berfungsi dengan benar. " +
                    "Error: {} ===", e.getMessage(), e);
            // Kita lempar ulang agar aplikasi gagal startup jika data tidak bisa dimuat
            throw new RuntimeException("Gagal memuat data awal dari Frankfurter API", e);
        }
    }
}
