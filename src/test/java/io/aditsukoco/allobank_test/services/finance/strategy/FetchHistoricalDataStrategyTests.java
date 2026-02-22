package io.aditsukoco.allobank_test.services.finance.strategy;

import io.aditsukoco.allobank_test.models.dto.api_response.HistoricalDataAPIResponseDTO;
import io.aditsukoco.allobank_test.repositories.frankfurter.FrankfurterDataRepositoryInterface;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class FetchHistoricalDataStrategyTests {
    @Mock
    private FrankfurterDataRepositoryInterface mockFrankfurterDataRepository;

    @InjectMocks
    private FetchHistoricalDataStrategy fetchHistoricalDataStrategy;

    @Test
    public void fetchDataTest_DataAvailable() {
        HistoricalDataAPIResponseDTO returnedData = HistoricalDataAPIResponseDTO.builder().build();
        when(mockFrankfurterDataRepository.getHistoricalResponseData()).thenReturn(returnedData);

        HistoricalDataAPIResponseDTO result = fetchHistoricalDataStrategy.fetchData();
        assertEquals(returnedData, result);
    }
}
