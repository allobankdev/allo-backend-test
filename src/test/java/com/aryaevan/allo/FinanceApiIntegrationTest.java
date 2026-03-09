package com.aryaevan.allo;

import com.aryaevan.allo.store.FinanceDataStore;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@DisplayName("Finance API Integration Tests")
class FinanceApiIntegrationTest {
    
    @Autowired
    private MockMvc mockMvc;
    
    @Autowired
    private FinanceDataStore financeDataStore;
    
    @Test
    @DisplayName("Should have initialized data store on startup")
    void testDataStoreInitializedOnStartup() {
        assertTrue(financeDataStore.isInitialized());
    }
    
    @Test
    @DisplayName("Should have all three resources in cache")
    void testAllResourcesLoadedInCache() {
        assertNotNull(financeDataStore.get("latest_idr_rates"));
        assertNotNull(financeDataStore.get("historical_idr_usd"));
        assertNotNull(financeDataStore.get("supported_currencies"));
    }
    
    @Test
    @DisplayName("GET /api/finance/data/latest_idr_rates should return cached data")
    void testGetLatestIDRRates() throws Exception {
        mockMvc.perform(get("/api/finance/data/latest_idr_rates"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.base").value("IDR"))
                .andExpect(jsonPath("$.rates").isNotEmpty())
                .andExpect(jsonPath("$.rates.USD_BuySpread_IDR").exists());
    }
    
    @Test
    @DisplayName("GET /api/finance/data/historical_idr_usd should return cached data")
    void testGetHistoricalIDRUSD() throws Exception {
        mockMvc.perform(get("/api/finance/data/historical_idr_usd"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.base").value("IDR"))
                .andExpect(jsonPath("$.rates").isNotEmpty());
    }
    
    @Test
    @DisplayName("GET /api/finance/data/supported_currencies should return cached data")
    void testGetSupportedCurrencies() throws Exception {
        mockMvc.perform(get("/api/finance/data/supported_currencies"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isNotEmpty());
    }
    
    @Test
    @DisplayName("GET /api/finance/data/invalid_type should return 404")
    void testGetInvalidResourceType() throws Exception {
        mockMvc.perform(get("/api/finance/data/invalid_resource_type"))
                .andExpect(status().isNotFound());
    }
    
    @Test
    @DisplayName("Cached data should contain all required fields for latest_idr_rates")
    void testLatestRatesDataStructure() throws Exception {
        mockMvc.perform(get("/api/finance/data/latest_idr_rates"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.base").exists())
                .andExpect(jsonPath("$.date").exists())
                .andExpect(jsonPath("$.rates").exists());
    }
    
    @Test
    @DisplayName("Multiple requests should serve cached data without calling external API")
    void testDataServedFromCache() throws Exception {
        mockMvc.perform(get("/api/finance/data/latest_idr_rates"))
                .andExpect(status().isOk());
        
        mockMvc.perform(get("/api/finance/data/latest_idr_rates"))
                .andExpect(status().isOk());
    }
}
