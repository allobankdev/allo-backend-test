package com.allobankdev.exchangrate.integration;

import com.allobankdev.exchangrate.client.ApiClient;
import com.allobankdev.exchangrate.dto.LatestRateResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
public class ApiClientIntegrationTest {

    @Autowired
    private ApiClient client;

    @Test
    void testRealApiCall() {
        LatestRateResponse response = client.getLatestRates();

        assertNotNull(response);
        assertNotNull(response.getRates());
    }
}
