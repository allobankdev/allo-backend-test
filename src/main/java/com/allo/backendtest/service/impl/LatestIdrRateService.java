package com.allo.backendtest.service.impl;

import com.allo.backendtest.dto.frankfurter.LatestDto;
import com.allo.backendtest.helper.SpreadHelper;
import com.allo.backendtest.service.IdrDataFetcher;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import tools.jackson.databind.ObjectMapper;

import java.math.BigDecimal;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

@Service("latest_idr_rates")
public class LatestIdrRateService implements IdrDataFetcher {

    private final CompletableFuture<LatestDto> completable = new CompletableFuture<>();
    private static final String PATH_LATEST = "/latest?base=IDR";

    @Value("${github.username}")
    private String username;

    private final RestClient restClient;
    private final ObjectMapper mapper;

    public LatestIdrRateService(RestClient restClient,ObjectMapper mapper) {
        this.restClient = restClient;
        this.mapper = mapper;
    }

    @Override
    public void update() throws Exception {
        completable.complete(getData());
    }

    @Override
    public Object fetch() {
        return completable.join();
    }

    public LatestDto getData() throws Exception {
        Map<String, Object> result = restClient.get()
                .uri(PATH_LATEST)
                .retrieve()
                .body(new ParameterizedTypeReference<>() {
                });

        if(result == null) {
            throw new Exception("Failed : result not found");
        }

        Double usdRate = ((Map<String, Double>) result.get("rates")).get("USD");
        if(usdRate == null) {
            throw new Exception("Failed : USD rate not found");
        }

        BigDecimal buySpread = SpreadHelper.getSpread(username, BigDecimal.valueOf(usdRate));
        result.put("USD_BuySpread_IDR", buySpread);

        return mapper.convertValue(result, LatestDto.class);
    }

}
