package com.self.bs.source.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.self.bs.source.config.ExchangeRateProperties;
import com.self.bs.source.dto.response.ResponseDto;
import com.self.bs.source.service.ExchangeRateService;

@RestController
@RequestMapping("/api/finance/data/")
public class ExchangeRateController {
    @Autowired
    protected ExchangeRateService exchangeRateService;

    @Autowired
    protected ExchangeRateProperties exchangeRateProperties;

    @GetMapping("supported_currencies")
    public ResponseEntity<ResponseDto<Object>> supportedCurrencies(){
        return ResponseEntity.ok(exchangeRateService.getCurrencyList());
    }

    @GetMapping("historical_idr_usd")
    public ResponseEntity<ResponseDto<Object>> historicalIdrUsd(@RequestParam String dateFrom, @RequestParam String dateTo){
        return ResponseEntity.ok(exchangeRateService.getExchangeRateHistorical(dateFrom, dateTo, exchangeRateProperties.getBaseCurrency(), exchangeRateProperties.getTargetCurrency()));
    }

    @GetMapping("latest_idr_rates")
    public ResponseEntity<ResponseDto<Object>> latestIdrRates(){
        return ResponseEntity.ok(exchangeRateService.getLatestExchangeRate(exchangeRateProperties.getBaseCurrency(), exchangeRateProperties.getTargetCurrency()));
    }
}
