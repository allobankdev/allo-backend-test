package com.allobank.financeaggregator.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.allobank.financeaggregator.model.HistoricalRatesResponse;
import com.allobank.financeaggregator.model.LatestRatesResponse;
import com.allobank.financeaggregator.service.FrankfurterClient;
import java.math.BigDecimal;
import java.util.Map;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.reactive.function.client.WebClient;

@SpringBootTest
@AutoConfigureMockMvc
class FinanceDataControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @DynamicPropertySource
    static void registerProperties(DynamicPropertyRegistry registry) {
        registry.add("frankfurter.github-username", () -> "testuser");
        registry.add("finance.historical.start-date", () -> "2024-01-01");
        registry.add("finance.historical.end-date", () -> "2024-01-05");
        registry.add("finance.historical.from", () -> "IDR");
        registry.add("finance.historical.to", () -> "USD");
    }

    @Test
    void latestEndpointReturnsSpread() throws Exception {
        mockMvc.perform(get("/api/finance/data/latest_idr_rates"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data[0].resourceType").value("latest_idr_rates"))
                .andExpect(jsonPath("$.data[0].data.base").value("IDR"))
                .andExpect(jsonPath("$.data[0].data.USD_BuySpread_IDR").exists());
    }

    @Test
    void historicalEndpointReturnsFilteredRange() throws Exception {
        mockMvc.perform(get("/api/finance/data/historical_idr_usd"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data[0].resourceType").value("historical_idr_usd"))
                .andExpect(jsonPath("$.data[0].data.start_date").value("2024-01-01"))
                .andExpect(jsonPath("$.data[0].data.end_date").value("2024-01-05"))
                .andExpect(jsonPath("$.data[0].data.rates.2024-01-02.USD").value(0.000064));
    }

    @Test
    void supportedCurrenciesEndpointReturnsMap() throws Exception {
        mockMvc.perform(get("/api/finance/data/supported_currencies"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data[0].resourceType").value("supported_currencies"))
                .andExpect(jsonPath("$.data[0].data.USD").value("United States Dollar"));
    }

    @Test
    void unknownResourceReturnsNotFound() throws Exception {
        mockMvc.perform(get("/api/finance/data/unknown"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.error").exists());
    }

    @TestConfiguration
    static class StubClientConfig {

        @Bean
        @Primary
        FrankfurterClient frankfurterClient() {
            LatestRatesResponse latest = new LatestRatesResponse(
                    new BigDecimal("1.0"),
                    "IDR",
                    "2024-01-05",
                    Map.of("USD", new BigDecimal("0.000065"))
            );
            HistoricalRatesResponse historical = new HistoricalRatesResponse(
                    new BigDecimal("1.0"),
                    "IDR",
                    "2023-12-29",
                    "2024-01-05",
                    Map.of(
                            "2023-12-29", Map.of("USD", new BigDecimal("0.000065")),
                            "2024-01-02", Map.of("USD", new BigDecimal("0.000064"))
                    )
            );
            Map<String, String> currencies = Map.of(
                    "USD", "United States Dollar",
                    "IDR", "Indonesian Rupiah"
            );

            return new StubFrankfurterClient(latest, historical, currencies);
        }
    }

    private static class StubFrankfurterClient extends FrankfurterClient {

        private final LatestRatesResponse latest;
        private final HistoricalRatesResponse historical;
        private final Map<String, String> currencies;

        private StubFrankfurterClient(
                LatestRatesResponse latest,
                HistoricalRatesResponse historical,
                Map<String, String> currencies
        ) {
            super(WebClient.builder().build());
            this.latest = latest;
            this.historical = historical;
            this.currencies = currencies;
        }

        @Override
        public <T> T get(String path, Class<T> responseType) {
            if (path.startsWith("/latest")) {
                return responseType.cast(latest);
            }
            if (path.startsWith("/2024-01-01..2024-01-05")) {
                return responseType.cast(historical);
            }
            throw new IllegalArgumentException("Unexpected path: " + path);
        }

        @Override
        @SuppressWarnings("unchecked")
        public <T> T get(String path, ParameterizedTypeReference<T> responseType) {
            if (path.equals("/currencies")) {
                return (T) currencies;
            }
            throw new IllegalArgumentException("Unexpected path: " + path);
        }
    }
}
