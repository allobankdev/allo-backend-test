package com.allobank.allo_backend_test.finance.service;

import com.allobank.allo_backend_test.finance.service.SpreadService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SpreadServiceTest {

    @Mock
    private SpreadService spreadService;

    @Test
    void shouldCalculateSpread() {
        when(spreadService.calculateSpread(0.0001)).thenReturn(0.00002);

        Double result = spreadService.calculateSpread(0.0001);

        assertEquals(0.00002, result);
        verify(spreadService).calculateSpread(0.0001);
    }

    @Test
    void shouldReturnSpreadFactor() {
        when(spreadService.getSpreadFactor()).thenReturn(0.0002);

        Double factor = spreadService.getSpreadFactor();

        assertEquals(0.0002, factor, 0.00001);
        verify(spreadService).getSpreadFactor();
    }
}