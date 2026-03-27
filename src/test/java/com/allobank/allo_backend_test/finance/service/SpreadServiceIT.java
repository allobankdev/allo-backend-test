package com.allobank.allo_backend_test.finance.service;

import com.allobank.allo_backend_test.finance.config.AppConfig;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class SpreadServiceIT {

    @Autowired
    private SpreadService spreadService;

    @Autowired
    private AppConfig appConfig;

    @Test
    void shouldHaveCorrectSpreadFactor() {
        int sum = appConfig.getGithubUsername().toLowerCase().chars().sum();
        double expected = (sum % 1000) / 100000.0;

        assertNotNull(spreadService.getSpreadFactor());
        assertEquals(expected, spreadService.getSpreadFactor(), 0.0001);
    }

    @Test
    void shouldCalculatePositiveSpread() {
        Double result = spreadService.calculateSpread(0.000064);
        assertTrue(result > 0.0);
        assertTrue(result > 15000.0);
    }
}