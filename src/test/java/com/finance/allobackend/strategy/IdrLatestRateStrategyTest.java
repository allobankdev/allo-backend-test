package com.finance.allobackend.strategy;

import com.fasterxml.jackson.databind.JsonNode;
import com.finance.allobackend.strategy.impl.HistoryIdrToUsdStrategyImpl;
import com.finance.allobackend.strategy.impl.IdrLatestRateStrategyImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestTemplate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

public class IdrLatestRateStrategyTest {
    private IdrLatestRateStrategyImpl strategy;
    private RestTemplate restTemplate;
    private MockRestServiceServer mockServer;

    @BeforeEach
    void setUp() {
        restTemplate = new RestTemplate();
        mockServer = MockRestServiceServer.bindTo(restTemplate).build();
        strategy = new IdrLatestRateStrategyImpl();
    }

    @Test
    void testGetOrRefreshData_GetCorrectly(){
        String jsonResp  = "{\n" +
                "  \"amount\": 1,\n" +
                "  \"base\": \"IDR\",\n" +
                "  \"date\": \"2025-11-19\",\n" +
                "  \"rates\": {\n" +
                "    \"AUD\": 0.000092,\n" +
                "    \"BGN\": 0.0001,\n" +
                "    \"BRL\": 0.00032,\n" +
                "    \"CAD\": 0.000084,\n" +
                "    \"CHF\": 0.000048,\n" +
                "    \"CNY\": 0.00043,\n" +
                "    \"CZK\": 0.00125,\n" +
                "    \"DKK\": 0.00039,\n" +
                "    \"EUR\": 0.000052,\n" +
                "    \"GBP\": 0.000046,\n" +
                "    \"HKD\": 0.00047,\n" +
                "    \"HUF\": 0.01974,\n" +
                "    \"ILS\": 0.0002,\n" +
                "    \"INR\": 0.00529,\n" +
                "    \"ISK\": 0.00758,\n" +
                "    \"JPY\": 0.00934,\n" +
                "    \"KRW\": 0.08769,\n" +
                "    \"MXN\": 0.0011,\n" +
                "    \"MYR\": 0.00025,\n" +
                "    \"NOK\": 0.00061,\n" +
                "    \"NZD\": 0.00011,\n" +
                "    \"PHP\": 0.00352,\n" +
                "    \"PLN\": 0.00022,\n" +
                "    \"RON\": 0.00026,\n" +
                "    \"SEK\": 0.00057,\n" +
                "    \"SGD\": 0.000078,\n" +
                "    \"THB\": 0.00194,\n" +
                "    \"TRY\": 0.00253,\n" +
                "    \"USD\": 0.00006,\n" +
                "    \"ZAR\": 0.00103\n" +
                "  },\n" +
                "  \"buySpreadUSDtoIDR\": 16801,\n" +
                "  \"calculationOwner\": \"nikods761\",\n" +
                "  \"spreadFactor\": 0.00806\n" +
                "}";

        mockServer.expect(requestTo("/latest?base=IDR"))
                .andRespond(withSuccess(jsonResp, MediaType.APPLICATION_JSON));
        strategy.getOrRefreshData(restTemplate);
        mockServer.verify();

        JsonNode cachedData = (JsonNode) strategy.getCacheData();
        assertNotNull(cachedData);
    }
    @Test
    void testGetResourceType_ReturnsCorrectIdentifier() {
        assertEquals("latestIDRRate", strategy.getResourceType());
    }
}
