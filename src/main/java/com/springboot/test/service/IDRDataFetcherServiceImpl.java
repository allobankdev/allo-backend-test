package com.springboot.test.service;

import com.springboot.test.calculate.CalculateBuySpread;
import com.springboot.test.dto.HistoricalDTO;
import com.springboot.test.dto.LatestRateDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class IDRDataFetcherServiceImpl implements IDRDataFetcherService {

    @Autowired
    ApiService service;

    @Override
    public LatestRateDTO getLatestIdrRate() {
        LatestRateDTO response = service.getDataLatestRate();
        response.setUSDBuySpreadIDR(new CalculateBuySpread().getUSDBuySpreadIDR(response.getRates().get("USD")));

        return response;
    }

    @Override
    public HistoricalDTO getHistoricalIdrUsd() {
        HistoricalDTO response = service.getHistoricalIdrUsd();
        return response;
    }

    @Override
    public String getSupportedCurrencies() {
        String response = service.getSupportedCurrencies();
        return response;
    }

}
