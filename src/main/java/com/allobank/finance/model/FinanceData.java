package com.allobank.finance.model;

public sealed interface FinanceData permits HistoricalRateData, LatestRateData, SupportedCurrenciesData {
}
