package com.bank.allo.client.fetcher;

import com.bank.allo.repository.outbound.FrankfurterClientRepository;
import com.bank.allo.usecase.idr.IdrDataFetcher;
import com.bank.allo.utils.IdrRateMapperUtils;
import java.util.Map;

public class HistoricalIdrUsdFetcher implements IdrDataFetcher {

    private final FrankfurterClientRepository repo;

    public HistoricalIdrUsdFetcher(FrankfurterClientRepository repo) {
        this.repo = repo;
    }

    @Override
    public String resourceType() {
        return "historical_idr_usd";
    }

    @Override
    public Object fetch() {
        Map<String, Object> raw = repo.fetchHistoricalIdrUsd();
        return IdrRateMapperUtils.toHistoricalRates(raw);
    }
}
