package com.allobankdev.exchangrate.controller;

import com.allobankdev.exchangrate.dto.CurrencyResponse;
import com.allobankdev.exchangrate.service.store.DataStore;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ExchangeRateControllerTest {

    @Mock
    private DataStore store;

    @InjectMocks
    private ExchangeRateController controller;

    @Test
    void get() {
            String resourceType = "supported_currencies";
            CurrencyResponse mockResponse = new CurrencyResponse();

            when(store.get(resourceType)).thenReturn(mockResponse);

            ResponseEntity<?> response = controller.get(resourceType);

            assertEquals(200, response.getStatusCode().value());
            assertNotNull(response.getBody());
    }
}