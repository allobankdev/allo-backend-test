package id.co.microservice.currency.currency_service.constant;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CurrencyConstantTest {

    @Test
    void testLatestIdrRatesConstant() {
        assertEquals("latest_idr_usd", CurrencyConstant.LATEST_IDR_RATES);
    }

    @Test
    void testHistoricalIdrUsdConstant() {
        assertEquals("historical_idr_usd", CurrencyConstant.HISTORICAL_IDR_USD);
    }

    @Test
    void testSupportedCurrenciesConstant() {
        assertEquals("supported_currencies", CurrencyConstant.SUPPORTED_CURRENCIES);
    }

}