package com.example.allobank_backend_test.Service;

public sealed interface IDRDataFetcher permits LatestRatesFetcher, HistoricalFetcher, CurrencyFetcher {
    String getType();
    Object fetch();
}
