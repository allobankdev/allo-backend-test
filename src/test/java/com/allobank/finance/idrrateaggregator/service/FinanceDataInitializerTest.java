package com.allobank.finance.idrrateaggregator.service;

import com.allobank.finance.idrrateaggregator.model.dto.FinanceResponse;
import com.allobank.finance.idrrateaggregator.startegy.IDRDataFetcher;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.ApplicationRunner;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class FinanceDataInitializerTest {

    @Mock
    private FinanceDataStore store;

    @Mock
    private IDRDataFetcher latestFetcher;

    @Mock
    private IDRDataFetcher historicalFetcher;

    @Test
    void shouldLoadDataFromAllStrategiesAndInitializeStore() throws Exception {
        // given
        when(latestFetcher.getResourceType())
                .thenReturn("latest_idr_rates");
        when(latestFetcher.fetch())
                .thenReturn(List.of(
                        new FinanceResponse("USD", 0.00006)
                ));

        when(historicalFetcher.getResourceType())
                .thenReturn("historical_idr_usd");
        when(historicalFetcher.fetch())
                .thenReturn(List.of(
                        new FinanceResponse("2024-01-05", 0.000064)
                ));

        Map<String, IDRDataFetcher> strategies = Map.of(
                "latest", latestFetcher,
                "historical", historicalFetcher
        );

        FinanceDataInitializer initializer = new FinanceDataInitializer();
        ApplicationRunner runner =
                initializer.loadData(strategies, store);

        // when
        runner.run(null);

        // then
        ArgumentCaptor<Map<String, List<FinanceResponse>>> captor =
                ArgumentCaptor.forClass(Map.class);

        verify(store).initialize(captor.capture());

        Map<String, List<FinanceResponse>> loadedData =
                captor.getValue();

        assertThat(loadedData)
                .containsKeys("latest_idr_rates", "historical_idr_usd");

        assertThat(loadedData.get("latest_idr_rates"))
                .hasSize(1);

        assertThat(loadedData.get("historical_idr_usd"))
                .hasSize(1);
    }
}
