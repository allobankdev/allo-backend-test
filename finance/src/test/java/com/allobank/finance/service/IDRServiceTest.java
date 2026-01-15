package com.allobank.finance.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.allobank.finance.store.FinanceDataStore;

@ExtendWith(MockitoExtension.class)
class IDRServiceTest {

    @Mock
    FinanceDataStore dataStore;

    @InjectMocks
    IDRService service;

    @Test
    void shouldReturnDataFromStore() {
        when(dataStore.get("latest_idr_rates")).thenReturn("DATA");

        Object result = service.getData("latest_idr_rates");

        assertEquals("DATA", result);
    }
}
