package io.aditsukoco.allobank_test.services.finance.strategy;

import io.aditsukoco.allobank_test.repositories.frankfurter.FrankfurterDataRepositoryInterface;
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
public class FetchCurrenciesDataStrategyTests {
    @Mock
    private FrankfurterDataRepositoryInterface mockFrankfurterDataRepository;

    @InjectMocks
    private FetchCurrenciesDataStrategy fetchCurrenciesDataStrategy;

    @Test
    public void fetchDataTest() {
        Map<String, String> returnedData = new HashMap<>();
        when(mockFrankfurterDataRepository.getCurrencies()).thenReturn(returnedData);

        Map<String, String> result = fetchCurrenciesDataStrategy.fetchData();

        assertEquals(returnedData, result);
    }
}
