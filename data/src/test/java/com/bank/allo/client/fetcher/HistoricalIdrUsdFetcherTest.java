package com.bank.allo.client.fetcher;

import com.bank.allo.repository.outbound.FrankfurterClientRepository;
import com.bank.allo.domain.idr.HistoricalRates;
import org.junit.jupiter.api.Test;
import java.util.Map;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class HistoricalIdrUsdFetcherTest {

    @Test
    void testResourceType() {
        FrankfurterClientRepository repo = mock(FrankfurterClientRepository.class);
        HistoricalIdrUsdFetcher fetcher = new HistoricalIdrUsdFetcher(repo);

        assertEquals("historical_idr_usd", fetcher.resourceType());
    }

    @Test
    void testFetchReturnsHistoricalRates() {
        FrankfurterClientRepository repo = mock(FrankfurterClientRepository.class);

        Map<String, Object> fakeResponse = Map.of(
                "start_date", "2024-01-01",
                "end_date", "2024-01-03",
                "rates", Map.of(
                        "2024-01-01", Map.of("USD", 0.000065),
                        "2024-01-02", Map.of("USD", 0.000066)
                )
        );

        when(repo.fetchHistoricalIdrUsd()).thenReturn(fakeResponse);

        HistoricalIdrUsdFetcher fetcher = new HistoricalIdrUsdFetcher(repo);

        Object result = fetcher.fetch();

        assertNotNull(result);
        assertTrue(result instanceof HistoricalRates);

        HistoricalRates hr = (HistoricalRates) result;

        assertEquals("2024-01-01", hr.getStartDate());
        assertEquals("2024-01-03", hr.getEndDate());
        assertEquals(0.000065, hr.getRates().get("2024-01-01").get("USD"));

        verify(repo, times(1)).fetchHistoricalIdrUsd();
    }
}
