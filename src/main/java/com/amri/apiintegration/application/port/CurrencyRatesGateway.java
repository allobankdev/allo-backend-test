package com.amri.apiintegration.application.port;

import com.amri.apiintegration.dto.frankfurter.CurrenciesDto;
import com.amri.apiintegration.dto.frankfurter.HistoricalRatesDto;
import com.amri.apiintegration.dto.frankfurter.LatestRatesDto;

public interface CurrencyRatesGateway {
    LatestRatesDto getLatestRates(String base);

    HistoricalRatesDto getHistoricalRates(String startDate, String endDate, String from, String to);

    CurrenciesDto getCurrencies();
}
