package com.allobank.allo_backend_test.finance.runner;

import com.allobank.allo_backend_test.finance.MockDataSourceClient;
import com.allobank.allo_backend_test.finance.model.CurrenciesModel;
import com.allobank.allo_backend_test.finance.model.HistoricalRatesModel;
import com.allobank.allo_backend_test.finance.model.LatestRatesModel;
import com.allobank.allo_backend_test.finance.repository.FinanceRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Import(MockDataSourceClient.class)
class PreloadDataIT {

    @Autowired
    private FinanceRepository repository;

    @Test
    void shouldLoadAllThree() {
        Map<String, ?> data = repository.getData();

        assertNotNull(data);
        assertEquals(3, data.size());
        assertTrue(data.containsKey("latest_idr_rates"));
        assertTrue(data.containsKey("historical_idr_usd"));
        assertTrue(data.containsKey("supported_currencies"));
    }

    @Test
    void shouldBeImmutable() {
        assertThrows(Exception.class,
                () -> repository.getData().put("new_key", new CurrenciesModel(Map.of())));
    }

    @Test
    void shouldHaveCorrectHistoricalRates() {
        HistoricalRatesModel model = (HistoricalRatesModel) repository.get("historical_idr_usd");
        assertNotNull(model);
        assertEquals("IDR", model.base());
    }

    @Test
    void shouldHaveSupportedCurrencies() {
        CurrenciesModel model = (CurrenciesModel) repository.get("supported_currencies");
        assertNotNull(model);
        assertFalse(model.currencies().isEmpty());
    }
}