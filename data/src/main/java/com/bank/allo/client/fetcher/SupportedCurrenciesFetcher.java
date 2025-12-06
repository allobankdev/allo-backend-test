package com.bank.allo.client.fetcher;

import com.bank.allo.repository.outbound.FrankfurterClientRepository;
import com.bank.allo.usecase.idr.IdrDataFetcher;
import com.bank.allo.utils.IdrRateMapperUtils;
import java.util.Map;

public class SupportedCurrenciesFetcher implements IdrDataFetcher {

    private final FrankfurterClientRepository repo;

    public SupportedCurrenciesFetcher(FrankfurterClientRepository repo) {
        this.repo = repo;
    }

    @Override
    public String resourceType() {
        return "supported_currencies";
    }

    @Override
    public Object fetch() {
        Map<String, String> raw = repo.fetchSupportedCurrencies();
        return IdrRateMapperUtils.toSupportedCurrencies(raw);
    }
}
