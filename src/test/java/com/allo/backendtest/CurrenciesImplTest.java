package com.allo.backendtest;

import com.allo.backendtest.client.FrankfurterClient;
import com.allo.backendtest.dto.frankfurter.CurrenciesDto;
import com.allo.backendtest.dto.properties.GithubProperties;
import com.allo.backendtest.service.impl.CurrenciesImpl;
import com.allo.backendtest.store.BaseStore;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import tools.jackson.databind.ObjectMapper;

import java.io.InputStream;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@Slf4j
@ExtendWith(MockitoExtension.class)
public class CurrenciesImplTest {

    @Mock
    private BaseStore<CurrenciesDto> currenciesStore;

    @Mock
    private FrankfurterClient frankfurterClient;

    @Mock
    private ObjectMapper mapper;

    @InjectMocks
    private CurrenciesImpl service;

    @Test
    void fetchAndStoreData_storesDto() throws Exception {

        var localMapper = new ObjectMapper();

        InputStream inputStream = getClass()
                .getClassLoader()
                .getResourceAsStream("currencies_response.json");

        Map<String, Object> data = localMapper.readValue(inputStream, Map.class);
        log.info("mock response : {}", localMapper.writeValueAsString(data));

        when(frankfurterClient.getCurrencies()).thenReturn(data);

        CurrenciesDto mappedDto = mock(CurrenciesDto.class);
        when(mapper.convertValue(any(), eq(CurrenciesDto.class))).thenReturn(mappedDto);

        // act
        service.fetchAndStoreData();

        // assert mapper received a map containing the spread key
        ArgumentCaptor<Map> mapCaptor = ArgumentCaptor.forClass(Map.class);
        verify(mapper).convertValue(mapCaptor.capture(), eq(CurrenciesDto.class));
        Map captured = mapCaptor.getValue();
        log.info("captured : {}", captured);
        assertNotNull(captured);
        assertTrue(captured.containsKey("mapCurrencies"));
        log.info("CurrenciesDto : {}", localMapper.writeValueAsString(captured));

        // assert store was called with the mapped DTO returned by mapper
        verify(currenciesStore, times(1)).setData(mappedDto);

    }

    @Test
    void fetchAndStoreData_handlesErrorResponse_storeDto() {
        // read error response fixture
        var localMapper = new ObjectMapper();
        InputStream errorStream = getClass()
                .getClassLoader()
                .getResourceAsStream("error_response.json");

        Map<String, Object> errorData = localMapper.readValue(errorStream, Map.class);
        log.info("mock error response : {}", localMapper.writeValueAsString(errorData));

        when(frankfurterClient.getCurrencies()).thenReturn(errorData);

        assertDoesNotThrow(() -> service.fetchAndStoreData());

        verify(mapper, times(1)).convertValue(any(), eq(CurrenciesDto.class));
        verify(currenciesStore, times(1)).setData(any());
    }

    @Test
    void fetchAndStoreData_missingPayload_doesNotStore() {

        when(frankfurterClient.getCurrencies()).thenReturn(null);

        // Expect exception to propagate and ensure store is not called
        assertThrows(Exception.class, () -> service.fetchAndStoreData());

        verify(mapper, never()).convertValue(any(), eq(CurrenciesDto.class));
        verify(currenciesStore, never()).setData(any());
    }

    @Test
    void fetchAndStoreData_throwsWhenMapperFails_andDoesNotStore() {
        // normal payload but mapper fails
        var localMapper = new ObjectMapper();
        InputStream inputStream = getClass()
                .getClassLoader()
                .getResourceAsStream("currencies_response.json");

        Map<String, Object> data = localMapper.readValue(inputStream, Map.class);
        log.info("mock response : {}", localMapper.writeValueAsString(data));

        when(frankfurterClient.getCurrencies()).thenReturn(data);

        when(mapper.convertValue(any(), eq(CurrenciesDto.class))).thenThrow(new RuntimeException("mapper error"));

        // Expect exception to propagate and ensure store is not called
        assertThrows(RuntimeException.class, () -> service.fetchAndStoreData());

        verify(currenciesStore, never()).setData(any());
    }

}
