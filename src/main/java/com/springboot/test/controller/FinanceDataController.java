package com.springboot.test.controller;

import com.springboot.test.dto.HistoricalDTO;
import com.springboot.test.dto.LatestRateDTO;
import com.springboot.test.service.IDRDataFetcherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class FinanceDataController {

    @Autowired
    private IDRDataFetcherService idrDataFetcherService;

    @GetMapping("/finance/data/{resourceType}")
    public ResponseEntity<Object> getFinanceData(@PathVariable("resourceType") String resourceType) {

        try{
            switch (resourceType) {
                case "latest_idr_rates":
                    LatestRateDTO latestRateDTO = idrDataFetcherService.getLatestIdrRate();
                    return new ResponseEntity<>(latestRateDTO, HttpStatus.OK);
                case "historical_idr_usd":
                    HistoricalDTO historicalDTO = idrDataFetcherService.getHistoricalIdrUsd();
                    return new ResponseEntity<>(historicalDTO, HttpStatus.OK);
                case "supported_currencies":
                    String supportedCurrDto = idrDataFetcherService.getSupportedCurrencies();
                    return new ResponseEntity<>(supportedCurrDto, HttpStatus.OK);
                default:
                    return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

}
