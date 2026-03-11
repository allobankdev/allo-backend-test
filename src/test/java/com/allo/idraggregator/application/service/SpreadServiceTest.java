package com.allo.idraggregator.application.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.allo.idraggregator.infrastructure.config.properties.GithubProperties;

class SpreadServiceTest {

    @Mock
    private GithubProperties properties;

    private SpreadService service;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        service = new SpreadService(properties);
    }

    @Test
    void shouldCalculateSpreadFactorCorrectly() {
        
        when(properties.username()).thenReturn("username");

        double spreadFactor = service.getSpreadFactor();

        int sum = "username".chars().sum();
        double expected = (sum % 1000) / 100000.0;

        assertEquals(expected, spreadFactor);
    }

    @Test
    void shouldCalculateUsdBuySpreadCorrectly() {
        
        when(properties.username()).thenReturn("username");
        double rateUsd = 16000.0;

        double result = service.getUsdBuySpread(rateUsd);

        int sum = "username".chars().sum();
        double spreadFactor = (sum % 1000) / 100000.0;
        double expected = (1.0 / rateUsd) * (1 + spreadFactor);

        assertEquals(expected, result, 1e-10);
    }
}