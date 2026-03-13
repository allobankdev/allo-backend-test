package com.allobank.backend.test.service;

import com.allobank.backend.test.client.FrankfurterClient;
import com.allobank.backend.test.model.LatestRatesResponse;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

class FrankfurterServiceTest {

    @Test
    void testGetLatestRates_CalculatesSpread() {
        FrankfurterClient client = Mockito.mock(FrankfurterClient.class);
        FrankfurterService service = new FrankfurterService(client);

        LatestRatesResponse mockResponse = new LatestRatesResponse();
        mockResponse.setRates(Map.of("USD", 0.000064));

        when(client.getLatestRates()).thenReturn(mockResponse);

        LatestRatesResponse result = service.getLatestRates();

        assertNotNull(result.getUsdBuySpreadIdr());
        assertTrue(result.getUsdBuySpreadIdr() > 0);
    }
}