package com.example.allobank.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.allobank.service.ExchangeRateService;

@RestController
@RequestMapping("/api/finance/data")
public class ExchangeRateController {

    private final ExchangeRateService exchangeRateService;

    public ExchangeRateController(ExchangeRateService exchangeRateService) {
        this.exchangeRateService = exchangeRateService;
    }

    @GetMapping("/{resourceType}")
    public Object getData(@PathVariable String resourceType) {
        return exchangeRateService.getData(resourceType);
    }
}

