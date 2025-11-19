package com.allo.test.modules.finance.controller;

import com.allo.test.modules.finance.dto.res.FrankfurterCurrenciesResponse;
import com.allo.test.modules.finance.dto.res.FrankfurterHistoricalRatesResponse;
import com.allo.test.modules.finance.dto.res.LatestIDRRatesResponse;
import com.allo.test.modules.finance.enums.ResourceType;
import com.allo.test.modules.finance.service.DataStoreService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Integration tests for the Finance Controller endpoints.
 * <p>
 * Tests both success scenarios (retrieving valid resource types) and
 * error scenarios (invalid resource types that throw InvalidResourceTypeException).
 * <p>
 * Uses MockMvc for testing with JSONPath matchers for elegant assertions.
 */
@SpringBootTest
@AutoConfigureMockMvc
@DisplayName("Finance Controller Integration Tests")
class FinanceControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private DataStoreService dataStoreService;

    @BeforeEach
    void setUp() {
        // Clear any existing data
        dataStoreService.clearAll();

        // Pre-load mock data for testing
        loadMockData();
    }

    /**
     * Pre-loads the DataStoreService with mock data for testing.
     * This simulates what the StartupLoader does at application startup.
     */
    private void loadMockData() {
        // Mock Latest IDR Rates Response
        LatestIDRRatesResponse latestRates = new LatestIDRRatesResponse();
        latestRates.setAmount(BigDecimal.ONE);
        latestRates.setBase("IDR");
        latestRates.setDate(LocalDate.of(2025, 1, 19));

        Map<String, BigDecimal> rates = new HashMap<>();
        rates.put("USD", new BigDecimal("0.0000634"));
        rates.put("EUR", new BigDecimal("0.0000580"));
        rates.put("GBP", new BigDecimal("0.0000490"));
        latestRates.setRates(rates);
        latestRates.setUsdBuySpreadIdr(new BigDecimal("0.000786"));

        dataStoreService.store(ResourceType.LATEST_RATES, latestRates);

        // Mock Historical Rates Response
        FrankfurterHistoricalRatesResponse historicalRates = FrankfurterHistoricalRatesResponse.builder()
                .amount(BigDecimal.ONE)
                .base("IDR")
                .startDate(LocalDate.of(2024, 1, 1))
                .endDate(LocalDate.of(2024, 1, 5))
                .build();

        Map<LocalDate, Map<String, BigDecimal>> historicalData = new HashMap<>();
        Map<String, BigDecimal> jan1Rates = new HashMap<>();
        jan1Rates.put("USD", new BigDecimal("0.0000640"));
        historicalData.put(LocalDate.of(2024, 1, 1), jan1Rates);

        Map<String, BigDecimal> jan2Rates = new HashMap<>();
        jan2Rates.put("USD", new BigDecimal("0.0000635"));
        historicalData.put(LocalDate.of(2024, 1, 2), jan2Rates);

        historicalRates.setRates(historicalData);
        dataStoreService.store(ResourceType.HISTORICAL_RATES, historicalRates);

        // Mock Currencies Response
        Map<String, String> currencies = new HashMap<>();
        currencies.put("USD", "United States Dollar");
        currencies.put("EUR", "Euro");
        currencies.put("GBP", "British Pound Sterling");
        currencies.put("JPY", "Japanese Yen");

        FrankfurterCurrenciesResponse frankfurterCurrenciesResponse = FrankfurterCurrenciesResponse.builder()
                .currencies(currencies)
                .build();

        dataStoreService.store(ResourceType.CURRENCIES, frankfurterCurrenciesResponse);
    }

    // ==================== SUCCESS SCENARIOS ====================

    @Test
    @DisplayName("Should return latest IDR rates when resource type is 'latest_idr_rates'")
    void shouldReturnLatestIDRRates_WhenResourceTypeIsValid() throws Exception {
        // Act & Assert
        mockMvc.perform(get("/finance/data/latest_idr_rates"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").exists())
                .andExpect(jsonPath("$.data.amount").value(1))
                .andExpect(jsonPath("$.data.base").value("IDR"))
                .andExpect(jsonPath("$.data.date").exists())
                .andExpect(jsonPath("$.data.rates").exists())
                .andExpect(jsonPath("$.data.rates.USD").value(0.0000634))
                .andExpect(jsonPath("$.data.rates.EUR").value(0.0000580))
                .andExpect(jsonPath("$.data.rates.GBP").value(0.0000490))
                .andExpect(jsonPath("$.data.USD_BuySpread_IDR").exists())
                .andExpect(jsonPath("$.data.USD_BuySpread_IDR").value(0.000786));
    }

    @Test
    @DisplayName("Should return historical rates when resource type is 'historical_idr_usd'")
    void shouldReturnHistoricalRates_WhenResourceTypeIsValid() throws Exception {
        // Act & Assert
        mockMvc.perform(get("/finance/data/historical_idr_usd"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").exists())
                .andExpect(jsonPath("$.data.amount").value(1))
                .andExpect(jsonPath("$.data.base").value("IDR"))
                .andExpect(jsonPath("$.data.start_date").exists())
                .andExpect(jsonPath("$.data.end_date").exists())
                .andExpect(jsonPath("$.data.rates").exists())
                .andExpect(jsonPath("$.data.rates").isNotEmpty());
    }

    @Test
    @DisplayName("Should return supported currencies when resource type is 'supported_currencies'")
    void shouldReturnSupportedCurrencies_WhenResourceTypeIsValid() throws Exception {
        // Act & Assert
        mockMvc.perform(get("/finance/data/supported_currencies"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").exists())
                .andExpect(jsonPath("$.data.currencies").exists())
                .andExpect(jsonPath("$.data.currencies.USD").value("United States Dollar"))
                .andExpect(jsonPath("$.data.currencies.EUR").value("Euro"))
                .andExpect(jsonPath("$.data.currencies.GBP").value("British Pound Sterling"))
                .andExpect(jsonPath("$.data.currencies.JPY").value("Japanese Yen"));
    }

    // ==================== ERROR SCENARIOS ====================

    @Test
    @DisplayName("Should throw InvalidResourceTypeException when resource type is invalid")
    void shouldThrowInvalidResourceTypeException_WhenResourceTypeIsInvalid() throws Exception {
        // Act & Assert
        mockMvc.perform(get("/finance/data/invalid_type"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.responseSchema").exists())
                .andExpect(jsonPath("$.responseSchema.responseCode").value("invalid_resource_type"))
                .andExpect(jsonPath("$.responseSchema.responseMessage").value("Invalid Resource Type"));
    }
}
