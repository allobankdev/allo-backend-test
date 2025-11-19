package com.finance.allobackend.strategy;

import com.fasterxml.jackson.databind.JsonNode;
import com.finance.allobackend.strategy.impl.HistoryIdrToUsdStrategyImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestTemplate;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

public class HistoryIdrToUsdStrategyTest {
    private HistoryIdrToUsdStrategyImpl strategy;
    private RestTemplate restTemplate;
    private MockRestServiceServer mockServer;

    @BeforeEach
    void setUp() {
        restTemplate = new RestTemplate();
        mockServer = MockRestServiceServer.bindTo(restTemplate).build();
        strategy = new HistoryIdrToUsdStrategyImpl();
    }

    @Test
    void testGetOrRefreshData_GetCorrectly(){
        String jsonResp  = "{\n" +
                "  \"amount\": 1,\n" +
                "  \"base\": \"IDR\",\n" +
                "  \"start_date\": \"2023-12-29\",\n" +
                "  \"end_date\": \"2024-01-05\",\n" +
                "  \"rates\": {\n" +
                "    \"2023-12-29\": {\n" +
                "      \"USD\": 0.000065\n" +
                "    },\n" +
                "    \"2024-01-02\": {\n" +
                "      \"USD\": 0.000064\n" +
                "    },\n" +
                "    \"2024-01-03\": {\n" +
                "      \"USD\": 0.000064\n" +
                "    },\n" +
                "    \"2024-01-04\": {\n" +
                "      \"USD\": 0.000064\n" +
                "    },\n" +
                "    \"2024-01-05\": {\n" +
                "      \"USD\": 0.000064\n" +
                "    }\n" +
                "  }\n" +
                "}";

        mockServer.expect(requestTo("/2025-11-01..2025-11-05?from=IDR&to=USD"))
                .andRespond(withSuccess(jsonResp, MediaType.APPLICATION_JSON));
        strategy.getOrRefreshData(restTemplate);
        mockServer.verify();

        JsonNode cachedData = (JsonNode) strategy.getCacheData();
        assertNotNull(cachedData);
    }
    @Test
    void testGetResourceType_ReturnsCorrectIdentifier() {
        assertEquals("historyIDRToUSD", strategy.getResourceType());
    }
}
