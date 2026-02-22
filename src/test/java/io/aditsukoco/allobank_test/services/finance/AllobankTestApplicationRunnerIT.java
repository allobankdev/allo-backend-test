package io.aditsukoco.allobank_test.services.finance;

import io.aditsukoco.allobank_test.clients.frankfurter.FrankfurterHTTPClientInterface;
import io.aditsukoco.allobank_test.models.dto.api_response.HistoricalDataAPIResponseDTO;
import io.aditsukoco.allobank_test.models.dto.api_response.LatestAPIResponseDTO;
import io.aditsukoco.allobank_test.repositories.frankfurter.FrankfurterDataRepositoryInterface;
import io.aditsukoco.allobank_test.services.AllobankTestApplicationRunner;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.DefaultApplicationArguments;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.util.HashMap;
import java.util.Map;

import static org.mockito.Mockito.*;

@SpringBootTest
public class AllobankTestApplicationRunnerIT {

    @MockitoBean
    private FrankfurterHTTPClientInterface frankfurterHTTPClient;

    @MockitoBean
    private FrankfurterDataRepositoryInterface frankfurterDataRepository;

    @Autowired
    private AllobankTestApplicationRunner allobankTestApplicationRunner;

    @Test
    public void runTest() throws Exception {
        LatestAPIResponseDTO latestAPIResponse = LatestAPIResponseDTO.builder().build();
        when(frankfurterHTTPClient.fetchLatest(1, "IDR", "USD")).thenReturn(latestAPIResponse);

        HistoricalDataAPIResponseDTO historicalDataAPIResponse = HistoricalDataAPIResponseDTO.builder().build();
        when(frankfurterHTTPClient.fetchHistorical("IDR", "USD", "2024-01-01", "2024-01-05"))
                .thenReturn(historicalDataAPIResponse);

        Map<String, String> currenciesAPIResponse = new HashMap<>();
        when(frankfurterHTTPClient.fetchCurrencies()).thenReturn(currenciesAPIResponse);

        // run main function
        allobankTestApplicationRunner.run(new DefaultApplicationArguments());

        verify(frankfurterDataRepository, atLeastOnce()).setLatestResponseData(latestAPIResponse);
        verify(frankfurterDataRepository, atLeastOnce()).setHistoricalResponseData(historicalDataAPIResponse);
        verify(frankfurterDataRepository, atLeastOnce()).setCurrencies(currenciesAPIResponse);
    }
}
