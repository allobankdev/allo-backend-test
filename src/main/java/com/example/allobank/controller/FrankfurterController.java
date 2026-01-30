package com.example.allobank.controller;

import com.example.allobank.dto.LatestIDRResponse;
import com.example.allobank.dto.HistoricalRatesResponse;
import com.example.allobank.strategy.FrankfurterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class FrankfurterController {

    @Autowired
    FrankfurterService frankfurterService;

    @Value("${url.api.frankfurter.latest.base.idr}")
    private String urllatestbaseidr;

    @Value("${url.api.frankfurter.historical.range.date}")
    private String urlhistoricaldates;

    @Value("${url.api.frankfurter.currencies}")
    private String urlcurrencies;

    @GetMapping("latestidr")
    public LatestIDRResponse getLatestIDR()
    {
        LatestIDRResponse response = frankfurterService.getLatestBaseIDR(urllatestbaseidr);

        return response;
    }

    @GetMapping("historicldate")
    public HistoricalRatesResponse getHistoricalDateRange()
    {
        HistoricalRatesResponse response = frankfurterService.getHistoricalRatesDate(urlhistoricaldates);
        return response;
    }

    @GetMapping("currencies")
    public Map<String,String> getCurrencies()
    {
        Map<String,String> mapCurrencies  = frankfurterService.getCurrencies(urlcurrencies);
        return mapCurrencies;
    }
}
