package com.allobank.exercise.api.service.impl;


import com.allobank.exercise.api.service.IDRDataFetcher;
import org.springframework.stereotype.Service;

@Service("latest_idr_rates")
public class LatestIdrRateFetcher implements IDRDataFetcher {


    @Override
    public Object getData() {
        return null;
    }
}
