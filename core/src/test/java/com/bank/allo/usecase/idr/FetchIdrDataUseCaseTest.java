package com.bank.allo.usecase.idr;

import com.bank.allo.exception.BadRequestException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.Map;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class FetchIdrDataUseCaseTest {

    private IdrDataFetcher latestFetcher;
    private IdrDataFetcher histFetcher;
    private IdrDataFetcher supportedFetcher;
    private Map<String, IdrDataFetcher> registry;
    private FetchIdrDataUseCase useCase;

    @BeforeEach
    void setup() {
        latestFetcher = mock(IdrDataFetcher.class);
        histFetcher = mock(IdrDataFetcher.class);
        supportedFetcher = mock(IdrDataFetcher.class);

        registry = Map.of(
                "latest_idr_rates", latestFetcher,
                "historical_idr_usd", histFetcher,
                "supported_currencies", supportedFetcher
        );

        useCase = new FetchIdrDataUseCase(registry);
    }

    @Test
    void testExecuteReturnsResultFromFetcher() {
        when(latestFetcher.fetch()).thenReturn("RESULT_LATEST");

        var input = FetchIdrDataUseCase.InputValues.builder()
                .resourceType("latest_idr_rates")
                .build();

        var output = useCase.execute(input);

        assertEquals("RESULT_LATEST", output.getResult());
        verify(latestFetcher, times(1)).fetch();
    }

    @Test
    void testUnknownResourceTypeThrowsException() {
        var input = FetchIdrDataUseCase.InputValues.builder()
                .resourceType("unknown")
                .build();

        var ex = assertThrows(BadRequestException.class,
                () -> useCase.execute(input));

        assertEquals("Unknown resource type: unknown", ex.getMessage());
    }

    @Test
    void testCorrectFetcherIsSelected() {
        when(histFetcher.fetch()).thenReturn("HIST_RESULT");

        var input = FetchIdrDataUseCase.InputValues.builder()
                .resourceType("historical_idr_usd")
                .build();

        var output = useCase.execute(input);

        assertEquals("HIST_RESULT", output.getResult());
        verify(histFetcher, times(1)).fetch();
        verify(latestFetcher, never()).fetch();
        verify(supportedFetcher, never()).fetch();
    }

    @Test
    void testSupportedCurrenciesFetcherIsSelected() {
        when(supportedFetcher.fetch()).thenReturn("SUPPORTED_RESULT");

        var input = FetchIdrDataUseCase.InputValues.builder()
                .resourceType("supported_currencies")
                .build();

        var output = useCase.execute(input);

        assertEquals("SUPPORTED_RESULT", output.getResult());
        verify(supportedFetcher, times(1)).fetch();
        verify(latestFetcher, never()).fetch();
        verify(histFetcher, never()).fetch();
    }
}
