package com.bank.allo;

import com.bank.allo.repository.inbound.DataStore;
import com.bank.allo.runner.DataLoadRunner;
import com.bank.allo.usecase.idr.FetchIdrDataUseCase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.ApplicationArguments;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.ArgumentMatchers.argThat;

class DataLoadRunnerTest {

    private FetchIdrDataUseCase useCase;
    private DataStore store;
    private DataLoadRunner runner;

    @BeforeEach
    void setup() {
        useCase = mock(FetchIdrDataUseCase.class);
        store = mock(DataStore.class);
        runner = new DataLoadRunner(useCase, store);
    }

    @Test
    void run_shouldLoadAllThreeResourcesIntoStore() throws Exception {

        // mock output
        FetchIdrDataUseCase.OutputValues latest = mock(FetchIdrDataUseCase.OutputValues.class);
        FetchIdrDataUseCase.OutputValues historical = mock(FetchIdrDataUseCase.OutputValues.class);
        FetchIdrDataUseCase.OutputValues currencies = mock(FetchIdrDataUseCase.OutputValues.class);

        when(latest.getResult()).thenReturn("latestData");
        when(historical.getResult()).thenReturn("historicalData");
        when(currencies.getResult()).thenReturn("currenciesData");

        when(useCase.execute(argThat(a ->
                a != null && "latest_idr_rates".equals(a.getResourceType())
        ))).thenReturn(latest);

        when(useCase.execute(argThat(a ->
                a != null && "historical_idr_usd".equals(a.getResourceType())
        ))).thenReturn(historical);

        when(useCase.execute(argThat(a ->
                a != null && "supported_currencies".equals(a.getResourceType())
        ))).thenReturn(currencies);

        runner.run(mock(ApplicationArguments.class));

        verify(store).initialize(argThat(map ->
                map.size() == 3 &&
                "latestData".equals(map.get("latest_idr_rates")) &&
                "historicalData".equals(map.get("historical_idr_usd")) &&
                "currenciesData".equals(map.get("supported_currencies"))
        ));

        verify(useCase, times(3)).execute(any());
    }
}
