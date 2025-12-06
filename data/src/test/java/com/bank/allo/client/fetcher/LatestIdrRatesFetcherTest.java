package com.bank.allo.client.fetcher;

import com.bank.allo.repository.outbound.FrankfurterClientRepository;
import com.bank.allo.domain.idr.LatestRates;
import org.junit.jupiter.api.Test;
import java.util.Map;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class LatestIdrRatesFetcherTest {

    @Test
    void testResourceType() {
        FrankfurterClientRepository repo = mock(FrankfurterClientRepository.class);
        LatestIdrRatesFetcher fetcher = new LatestIdrRatesFetcher(repo, "putra");

        assertEquals("latest_idr_rates", fetcher.resourceType());
    }

    @Test
    void testFetchReturnsMappedLatestRates() {
        FrankfurterClientRepository repo = mock(FrankfurterClientRepository.class);

        Map<String, Object> fakeResponse = Map.of(
                "base", "IDR",
                "date", "2024-01-01",
                "rates", Map.of("USD", 0.000064)
        );

        when(repo.fetchLatestBaseIdr()).thenReturn(fakeResponse);

        LatestIdrRatesFetcher fetcher = new LatestIdrRatesFetcher(repo, "putra");

        Object result = fetcher.fetch();

        assertNotNull(result);
        assertTrue(result instanceof LatestRates);

        LatestRates rates = (LatestRates) result;

        assertEquals("IDR", rates.getBase());
        assertEquals("2024-01-01", rates.getDate());
        assertEquals(0.000064, rates.getRates().get("USD"));
        assertNotNull(rates.getUsdBuySpreadIdr());

        verify(repo, times(1)).fetchLatestBaseIdr();
    }
}
