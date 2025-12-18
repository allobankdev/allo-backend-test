package com.allo.backendtest.service.impl;

import com.allo.backendtest.dto.frankfurter.HistoricalDto;
import com.allo.backendtest.service.IdrDataFetcher;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestClient;

import java.util.concurrent.CompletableFuture;

@Service("historical_idr_usd")
public class HistoricalRateService implements IdrDataFetcher {

    private final CompletableFuture<HistoricalDto> completable = new CompletableFuture<>();

    private static final String PATH_HISTORICAL = "/%s?from=IDR&to=USD";

    @Value("${frankfurter.base-date-range}")
    private String HISTORICAL_DATE_RANGE;

    private final RestClient restClient;

    public HistoricalRateService(@Qualifier("frankfurterRestClientConfig") RestClient restClient) {
        this.restClient = restClient;
    }

    @Override
    public void update() throws Exception {
        completable.complete(getData(HISTORICAL_DATE_RANGE));
    }

    @Override
    public Object fetch() {
        return completable.join();
    }

    public HistoricalDto getData(String daterange) throws Exception {
        if(!StringUtils.hasText(daterange)) {
            throw new Exception("Invalid daterange");
        }

        HistoricalDto result = restClient.get()
                .uri( String.format(PATH_HISTORICAL, daterange))
                .retrieve()
                .body(HistoricalDto.class);

        if(result == null) {
            throw new Exception("Failed : result not found");
        }

        return result;
    }

}
