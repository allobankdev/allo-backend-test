package com.bank.allo.client.fetcher;

import com.bank.allo.repository.outbound.FrankfurterClientRepository;
import com.bank.allo.domain.idr.SupportedCurrencies;
import org.junit.jupiter.api.Test;
import java.util.Map;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class SupportedCurrenciesFetcherTest {

    @Test
    void testResourceType() {
        FrankfurterClientRepository repo = mock(FrankfurterClientRepository.class);
        SupportedCurrenciesFetcher fetcher = new SupportedCurrenciesFetcher(repo);

        assertEquals("supported_currencies", fetcher.resourceType());
    }

    @Test
    void testFetchReturnsSupportedCurrencies() {
        FrankfurterClientRepository repo = mock(FrankfurterClientRepository.class);

        Map<String, String> fakeCurrencies = Map.of(
                "USD", "United States Dollar",
                "IDR", "Indonesian Rupiah"
        );

        when(repo.fetchSupportedCurrencies()).thenReturn(fakeCurrencies);

        SupportedCurrenciesFetcher fetcher = new SupportedCurrenciesFetcher(repo);

        Object result = fetcher.fetch();

        assertNotNull(result);
        assertTrue(result instanceof SupportedCurrencies);

        SupportedCurrencies sc = (SupportedCurrencies) result;

        assertEquals("United States Dollar", sc.getCurrencies().get("USD"));
        assertEquals("Indonesian Rupiah", sc.getCurrencies().get("IDR"));

        verify(repo, times(1)).fetchSupportedCurrencies();
    }
}
