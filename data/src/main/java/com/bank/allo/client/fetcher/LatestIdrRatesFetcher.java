package com.bank.allo.client.fetcher;

import com.bank.allo.repository.outbound.FrankfurterClientRepository;
import com.bank.allo.usecase.idr.IdrDataFetcher;
import com.bank.allo.utils.IdrRateMapperUtils;
import java.util.Map;

public class LatestIdrRatesFetcher implements IdrDataFetcher {

    private final FrankfurterClientRepository repo;
    private final String githubUsername;

    public LatestIdrRatesFetcher(FrankfurterClientRepository repo, String githubUsername) {
        this.repo = repo;
        this.githubUsername = githubUsername;
    }

    @Override
    public String resourceType() {
        return "latest_idr_rates";
    }

    @Override
    public Object fetch() {
        Map<String, Object> raw = repo.fetchLatestBaseIdr();
        return IdrRateMapperUtils.toLatestRates(raw, githubUsername);
    }
}
