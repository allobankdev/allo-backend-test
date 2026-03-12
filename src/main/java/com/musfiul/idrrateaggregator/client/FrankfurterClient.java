package com.musfiul.idrrateaggregator.client;

import com.musfiul.idrrateaggregator.dto.CurrenciesResponse;
import com.musfiul.idrrateaggregator.dto.HistoricalRatesResponse;
import com.musfiul.idrrateaggregator.dto.LatestRatesResponse;

public interface FrankfurterClient {

    LatestRatesResponse getLatestRates();
    HistoricalRatesResponse getHistoricalRates();
    CurrenciesResponse getCurrencies();
}
