package io.aditsukoco.allobank_test.services.finance.strategy;

import io.aditsukoco.allobank_test.models.dto.api_response.LatestAPIResponseDTO;
import io.aditsukoco.allobank_test.models.dto.response.LatestDataResponseDTO;
import io.aditsukoco.allobank_test.repositories.frankfurter.FrankfurterDataRepositoryInterface;
import io.aditsukoco.allobank_test.repositories.spreadFactor.SpreadFactorDataRepositoryInterface;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class FetchLatestDataStrategyTests {

    @Mock
    private FrankfurterDataRepositoryInterface frankfurterDataRepository;

    @Mock
    private SpreadFactorDataRepositoryInterface spreadFactorDataRepository;

    @InjectMocks
    private FetchLatestDataStrategy fetchLatestDataStrategy;

    @Test
    public void fetchDataTest_AllDataAvailable() {
        Map<String, Float> mockRatesMap = new HashMap<>();
        mockRatesMap.put("USD", 0.5F);
        LatestAPIResponseDTO mockReturnedData = LatestAPIResponseDTO.builder()
                .amount(1)
                .base("IDR")
                .rates(mockRatesMap)
                .build();
        when(frankfurterDataRepository.getLatestResponseData()).thenReturn(mockReturnedData);
        when(spreadFactorDataRepository.getSpreadFactor()).thenReturn(0.5);

        // USD_BuySpread_IDR = (1 / Rate_USD) * (1 + Spread Factor)
        // USD_BuySpread_IDR = (1 / 0.5) * (1 + 0.5)
        // USD_BuySpread_IDR = 2 * 1.5
        // USD_BuySpread_IDR = 3
        LatestDataResponseDTO expectedData = LatestDataResponseDTO.builder()
                .amount(1)
                .base("IDR")
                .rates(mockRatesMap)
                .result(3)
                .build();

        LatestDataResponseDTO result = fetchLatestDataStrategy.fetchData();

        assertEquals(expectedData.getAmount(), result.getAmount());
        assertEquals(expectedData.getBase(), result.getBase());
        assertEquals(expectedData.getRates(), result.getRates());
        assertEquals(expectedData.getResult(), result.getResult());
    }
}
