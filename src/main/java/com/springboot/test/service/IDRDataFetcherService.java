package com.springboot.test.service;

import com.springboot.test.dto.HistoricalDTO;
import com.springboot.test.dto.LatestRateDTO;

public interface IDRDataFetcherService {
     LatestRateDTO getLatestIdrRate();
     HistoricalDTO getHistoricalIdrUsd();
     String getSupportedCurrencies();
}
