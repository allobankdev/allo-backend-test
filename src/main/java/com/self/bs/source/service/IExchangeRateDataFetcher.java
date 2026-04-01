package com.self.bs.source.service;

import com.self.bs.source.dto.request.ExchangeRateDataFetcherRequestDto;

public interface IExchangeRateDataFetcher {
    void fetchData(ExchangeRateDataFetcherRequestDto requestData);
}
