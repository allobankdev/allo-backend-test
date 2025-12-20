package com.allo.backendtest.service.impl;

import com.allo.backendtest.client.FrankfurterClient;
import com.allo.backendtest.dto.frankfurter.LatestDto;
import com.allo.backendtest.dto.properties.GithubProperties;
import com.allo.backendtest.helper.SpreadHelper;
import com.allo.backendtest.service.IdrDataFetcher;
import com.allo.backendtest.store.BaseStore;
import org.springframework.stereotype.Service;
import tools.jackson.databind.ObjectMapper;

import java.math.BigDecimal;
import java.util.Map;

@Service
public class LatestRateImpl implements IdrDataFetcher {

    private final BaseStore<LatestDto> latestStore;
    private final FrankfurterClient frankfurterClient;
    private final GithubProperties githubProperties;
    private final ObjectMapper mapper;

    public LatestRateImpl(BaseStore<LatestDto> latestStore, FrankfurterClient frankfurterClient, GithubProperties githubProperties, ObjectMapper mapper) {
        this.latestStore = latestStore;
        this.frankfurterClient = frankfurterClient;
        this.githubProperties = githubProperties;
        this.mapper = mapper;
    }

    @Override
    public void fetchAndStoreData() throws Exception {
        var data = frankfurterClient.getLatest();
        if (data == null) throw new Exception("Failed : data is null");

        Double usdRate = ((Map<String, Double>) data.get("rates")).get("USD");
        if (usdRate == null) throw new Exception("Failed : USD rate not found");

        var buySpread = SpreadHelper.getSpread(githubProperties.username(), BigDecimal.valueOf(usdRate));
        data.put("USD_BuySpread_IDR", buySpread);

        latestStore.setData(mapper.convertValue(data, LatestDto.class));
    }

}
