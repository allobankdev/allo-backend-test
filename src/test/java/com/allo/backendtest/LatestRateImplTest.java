package com.allo.backendtest;

import com.allo.backendtest.client.FrankfurterClient;
import com.allo.backendtest.dto.frankfurter.LatestDto;
import com.allo.backendtest.dto.properties.GithubProperties;
import com.allo.backendtest.service.impl.LatestRateImpl;
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
public class LatestRateImplTest {

    @Mock
    private BaseStore<LatestDto> latestStore;

    @Mock
    private GithubProperties githubProperties;

    @Mock
    private FrankfurterClient frankfurterClient;

    @Mock
    private ObjectMapper mapper;

    @InjectMocks
    private LatestRateImpl service;

    @Test
    void fetchAndStoreData_storesDtoAndInvokesSpreadHelper() throws Exception {

        var localMapper = new ObjectMapper();

        InputStream inputStream = getClass()
                .getClassLoader()
                .getResourceAsStream("latest_response.json");

        Map<String, Object> data = localMapper.readValue(inputStream, Map.class);
        log.info("mock response : {}", localMapper.writeValueAsString(data));

        when(frankfurterClient.getLatest()).thenReturn(data);
        when(githubProperties.username()).thenReturn("MrPandoyo");

        LatestDto mappedDto = mock(LatestDto.class);
        when(mapper.convertValue(any(), eq(LatestDto.class))).thenReturn(mappedDto);

        // act
        service.fetchAndStoreData();

        // assert mapper received a map containing the spread key
        ArgumentCaptor<Map> mapCaptor = ArgumentCaptor.forClass(Map.class);
        verify(mapper).convertValue(mapCaptor.capture(), eq(LatestDto.class));
        Map captured = mapCaptor.getValue();
        assertNotNull(captured);
        assertTrue(captured.containsKey("rates"));
        assertTrue(captured.containsKey("USD_BuySpread_IDR"));
        log.info("LatestDto : {}", localMapper.writeValueAsString(captured));

        // assert store was called with the mapped DTO returned by mapper
        verify(latestStore, times(1)).setData(mappedDto);

        // github username is consulted
        verify(githubProperties, times(1)).username();

    }

    @Test
    void fetchAndStoreData_handlesErrorResponse_doesNotStore() {
        // read error response fixture
        var localMapper = new ObjectMapper();
        InputStream errorStream = getClass()
                .getClassLoader()
                .getResourceAsStream("error_response.json");

        Map<String, Object> errorData = localMapper.readValue(errorStream, Map.class);
        log.info("mock error response : {}", localMapper.writeValueAsString(errorData));

        when(frankfurterClient.getLatest()).thenReturn(errorData);

        // Expect exception to propagate and ensure store is not called
        assertThrows(RuntimeException.class, () -> service.fetchAndStoreData());

        verify(mapper, never()).convertValue(any(), eq(LatestDto.class));
        verify(latestStore, never()).setData(any());
    }

    @Test
    void fetchAndStoreData_missingPayload_doesNotStore() {

        when(frankfurterClient.getLatest()).thenReturn(null);

        // Expect exception to propagate and ensure store is not called
        assertThrows(Exception.class, () -> service.fetchAndStoreData());

        verify(mapper, never()).convertValue(any(), eq(LatestDto.class));
        verify(latestStore, never()).setData(any());
    }

    @Test
    void fetchAndStoreData_missingRates_doesNotStore() throws Exception {
        // start from a valid response but remove rates
        var localMapper = new ObjectMapper();
        InputStream inputStream = getClass()
                .getClassLoader()
                .getResourceAsStream("latest_response.json");

        Map<String, Object> data = localMapper.readValue(inputStream, Map.class);
        data.remove("rates"); // simulate an incomplete payload

        when(frankfurterClient.getLatest()).thenReturn(data);

        // Expect exception to propagate and ensure store is not called
        assertThrows(RuntimeException.class, () -> service.fetchAndStoreData());

        verify(mapper, never()).convertValue(any(), eq(LatestDto.class));
        verify(latestStore, never()).setData(any());
    }

    @Test
    void fetchAndStoreData_throwsWhenMapperFails_andDoesNotStore() {
        // normal payload but mapper fails
        var localMapper = new ObjectMapper();
        InputStream inputStream = getClass()
                .getClassLoader()
                .getResourceAsStream("latest_response.json");

        Map<String, Object> data = localMapper.readValue(inputStream, Map.class);
        log.info("mock response : {}", localMapper.writeValueAsString(data));

        when(frankfurterClient.getLatest()).thenReturn(data);
        when(githubProperties.username()).thenReturn("MrPandoyo");

        when(mapper.convertValue(any(), eq(LatestDto.class))).thenThrow(new RuntimeException("mapper error"));

        // Expect exception to propagate and ensure store is not called
        assertThrows(RuntimeException.class, () -> service.fetchAndStoreData());

        verify(latestStore, never()).setData(any());
    }

}
