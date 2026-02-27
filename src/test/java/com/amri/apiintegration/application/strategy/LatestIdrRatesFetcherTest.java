package com.amri.apiintegration.application.strategy;

import com.amri.apiintegration.application.port.CurrencyRatesGateway;
import com.amri.apiintegration.application.service.SpreadFactorService;
import com.amri.apiintegration.config.ApplicationProperties;
import com.amri.apiintegration.dto.frankfurter.LatestRatesDto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class LatestIdrRatesFetcherTest {

    @Mock
    private CurrencyRatesGateway currencyRatesGateway;

    @Test
    void fetch_shouldCalculateUsdBuySpreadIdrAndWrapResult() {
        when(currencyRatesGateway.getLatestRates("IDR")).thenReturn(
                new LatestRatesDto(
                        "IDR",
                        "2026-02-26",
                        Map.of("USD", new BigDecimal("0.00006150")),
                        null
                )
        );

        SpreadFactorService spreadFactorService = new SpreadFactorService(new ApplicationProperties("Amri93"));
        LatestIdrRatesFetcher fetcher = new LatestIdrRatesFetcher(currencyRatesGateway, spreadFactorService);

        var result = fetcher.fetch();
        var data = (LatestRatesDto) result.data();

        assertEquals("latest_idr_rates", result.resourceType());
        assertEquals(new BigDecimal("16346.82926829"), data.usdBuySpreadIdr());
    }
}
