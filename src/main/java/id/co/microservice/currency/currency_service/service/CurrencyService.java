package id.co.microservice.currency.currency_service.service;

import id.co.microservice.currency.currency_service.dto.CurrencyResponseDto;
import id.co.microservice.currency.currency_service.dto.FrankfurterResponseDto;

public interface CurrencyService {

    /**
     * Get latest currency rates based on the provided base currency.
     * @param base the base currency code
     * @return FrankfurterResponseDto
     */
    CurrencyResponseDto getCurrencyLatestRates(String base);

    /**
     * Get historical currency rates.
     * @return FrankfurterResponseDto
     */
    CurrencyResponseDto getCurrencyHistoricalRates();

    /**
     * Get supported currencies.
     * @return FrankfurterResponseDto
     */
    CurrencyResponseDto getSupportedCurrencies();

}
