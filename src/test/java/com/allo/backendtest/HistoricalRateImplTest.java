package com.allo.backendtest;

import com.allo.backendtest.client.FrankfurterClient;
import com.allo.backendtest.dto.frankfurter.HistoricalDto;
import com.allo.backendtest.service.impl.HistoricalRateImpl;
import com.allo.backendtest.store.BaseStore;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import tools.jackson.databind.ObjectMapper;

import java.io.InputStream;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@Slf4j
@ExtendWith(MockitoExtension.class)
public class HistoricalRateImplTest {

    @Mock
    private BaseStore<HistoricalDto> historicalStore;

    @Mock
    private FrankfurterClient frankfurterClient;

    @Mock
    private ObjectMapper mapper;

    @InjectMocks
    private HistoricalRateImpl service;

    @Test
    void fetchAndStoreData_storesDto() throws Exception {

        var localMapper = new ObjectMapper();

        InputStream inputStream = getClass()
                .getClassLoader()
                .getResourceAsStream("historical_response.json");

        HistoricalDto data = localMapper.readValue(inputStream, HistoricalDto.class);
        log.info("mock response : {}", localMapper.writeValueAsString(data));

        when(frankfurterClient.getHistorical()).thenReturn(data);

        // act
        service.fetchAndStoreData();

        assertNotNull(data);
        assertTrue("IDR".equalsIgnoreCase(data.base()));
        assertFalse(data.rates().isEmpty());
        log.info("HistoricalDto : {}", localMapper.writeValueAsString(data));

        // assert store was called with the mapped DTO returned by mapper
        verify(historicalStore, times(1)).setData(data);

    }

    @Test
    void fetchAndStoreData_handlesErrorResponse_storeDto() {
        // read error response fixture
        var localMapper = new ObjectMapper();
        InputStream errorStream = getClass()
                .getClassLoader()
                .getResourceAsStream("error_response.json");

        HistoricalDto errorData = localMapper.readValue(errorStream, HistoricalDto.class);
        log.info("mock response : {}", localMapper.writeValueAsString(errorData));

        when(frankfurterClient.getHistorical()).thenReturn(errorData);

        assertDoesNotThrow(() -> service.fetchAndStoreData());

        verify(mapper, never()).convertValue(any(), eq(HistoricalDto.class));
        verify(historicalStore, times(1)).setData(errorData);
    }

    @Test
    void fetchAndStoreData_missingPayload_doesNotStore() {

        when(frankfurterClient.getHistorical()).thenReturn(null);

        // Expect exception to propagate and ensure store is not called
        assertThrows(Exception.class, () -> service.fetchAndStoreData());

        verify(mapper, never()).convertValue(any(), eq(HistoricalDto.class));
        verify(historicalStore, never()).setData(any());
    }

    @Test
    void fetchAndStoreData_storesMockedDto() throws Exception {
        // new: verify that when client returns a mocked DTO instance it's passed to the store
        HistoricalDto dto = mock(HistoricalDto.class);
        when(frankfurterClient.getHistorical()).thenReturn(dto);

        service.fetchAndStoreData();

        verify(historicalStore, times(1)).setData(dto);
        verifyNoMoreInteractions(mapper); // mapper shouldn't be used for DTO input
    }

    @Test
    void fetchAndStoreData_storeThrows_propagatesException() throws Exception {
        // new: ensure exceptions from the store propagate
        HistoricalDto dto = mock(HistoricalDto.class);
        when(frankfurterClient.getHistorical()).thenReturn(dto);

        doThrow(new RuntimeException("store failure")).when(historicalStore).setData(dto);

        RuntimeException ex = assertThrows(RuntimeException.class, () -> service.fetchAndStoreData());
        assertEquals("store failure", ex.getMessage());
        verify(historicalStore, times(1)).setData(dto);
    }

    @Test
    void fetchAndStoreData_emptyRates_storesDto() throws Exception {
        // new: ensure DTO with empty rates is still accepted and stored
        HistoricalDto dto = mock(HistoricalDto.class);
        when(frankfurterClient.getHistorical()).thenReturn(dto);

        assertDoesNotThrow(() -> service.fetchAndStoreData());
        verify(historicalStore, times(1)).setData(dto);
    }
}
