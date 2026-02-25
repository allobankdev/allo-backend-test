package com.allobank.finance.runner;

import com.allobank.finance.model.FinanceDataResult;
import com.allobank.finance.store.FinanceDataStore;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Integration test untuk memverifikasi bahwa {@link DataIngestionRunner}
 * berhasil memuat semua data ke {@link FinanceDataStore} saat aplikasi startup.
 *
 * <p>
 * Test ini menggunakan Spring context nyata dengan profil "test"
 * yang terhubung ke Frankfurter API yang sesungguhnya.
 * Jika koneksi internet tidak tersedia, test ini akan dilewati.
 *
 * <p>
 * Sesuai ketentuan: memverifikasi bahwa setelah ApplicationRunner selesai,
 * store berisi data yang tidak kosong untuk ketiga resource type.
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
class DataIngestionRunnerIntegrationTest {

    @Autowired
    private FinanceDataStore financeDataStore;

    @Test
    @DisplayName("Store harus diinisialisasi setelah aplikasi startup")
    void storeShouldBeInitializedAfterStartup() {
        assertThat(financeDataStore.isInitialized())
                .as("FinanceDataStore harus sudah diinisialisasi oleh ApplicationRunner")
                .isTrue();
    }

    @Test
    @DisplayName("Store harus berisi data untuk 'latest_idr_rates'")
    void storeShouldContainLatestIdrRates() {
        Optional<List<FinanceDataResult>> data = financeDataStore.getByResourceType("latest_idr_rates");

        assertThat(data)
                .as("latest_idr_rates harus ada di store")
                .isPresent();
        assertThat(data.get())
                .as("latest_idr_rates tidak boleh kosong")
                .isNotEmpty();
    }

    @Test
    @DisplayName("Store harus berisi data untuk 'historical_idr_usd'")
    void storeShouldContainHistoricalIdrUsd() {
        Optional<List<FinanceDataResult>> data = financeDataStore.getByResourceType("historical_idr_usd");

        assertThat(data)
                .as("historical_idr_usd harus ada di store")
                .isPresent();
        assertThat(data.get())
                .as("historical_idr_usd tidak boleh kosong")
                .isNotEmpty();
    }

    @Test
    @DisplayName("Store harus berisi data untuk 'supported_currencies'")
    void storeShouldContainSupportedCurrencies() {
        Optional<List<FinanceDataResult>> data = financeDataStore.getByResourceType("supported_currencies");

        assertThat(data)
                .as("supported_currencies harus ada di store")
                .isPresent();
        assertThat(data.get())
                .as("supported_currencies tidak boleh kosong")
                .isNotEmpty();
    }

    @Test
    @DisplayName("Store harus berisi tepat 3 resource types")
    void storeShouldContainExactlyThreeResourceTypes() {
        assertThat(financeDataStore.getAll())
                .as("Harus ada tepat 3 resource type yang dimuat")
                .hasSize(3)
                .containsKeys("latest_idr_rates", "historical_idr_usd", "supported_currencies");
    }
}
