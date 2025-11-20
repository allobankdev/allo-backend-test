package com.chikohakles.allobank.agregator.service;

import com.chikohakles.allobank.agregator.dto.Currency;
import com.chikohakles.allobank.agregator.dto.DateQueryResponse;
import com.chikohakles.allobank.agregator.dto.LatestResponse;

import java.util.Date;
import java.util.List;

public interface AgregatorService {
    public LatestResponse getLatest(String base);
    public DateQueryResponse getDateQuery(Date from, Date to, String base, String target);
    public List<Currency> getCurrencies();
}
