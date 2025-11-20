package com.chikohakles.allobank.agregator.service.impl;

import com.chikohakles.allobank.agregator.dto.Currency;
import com.chikohakles.allobank.agregator.dto.DateQueryResponse;
import com.chikohakles.allobank.agregator.dto.LatestResponse;
import com.chikohakles.allobank.agregator.service.AgregatorService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AgregatorServiceImpl implements AgregatorService {
    private final RestClient restClient;
    private static final String URL_LATEST = "/latest?base={base}";
    private static final String URL_DATE_QUERY = "/{from}..{to}?from={base}&to={target}";
    private static final String URL_CURRENCIES = "/currencies";

    @Override
    public LatestResponse getLatest(String base) {
        return restClient.get()
                .uri(URL_LATEST, base)
                .retrieve()
                .body(LatestResponse.class);
    }

    @Override
    public DateQueryResponse getDateQuery(Date from, Date to, String base, String target) {
        return restClient.get()
                .uri(URL_DATE_QUERY, from, to, base, target)
                .retrieve()
                .body(DateQueryResponse.class);
    }

    @Override
    public List<Currency> getCurrencies() {
        return restClient.get()
                .uri(URL_CURRENCIES)
                .retrieve()
                .body(List.class);
    }


}
