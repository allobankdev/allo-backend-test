package com.allobank.backend.test.controller;

import com.allobank.backend.test.model.DataStore;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class TestController {

    private final DataStore store;

    @GetMapping("/latest")
    public Object latest() {
        return store.getLatestRates();
    }

    @GetMapping("/currencies")
    public Object currencies() {
        return store.getCurrencies();
    }

    @GetMapping("/historical")
    public Object historical() {
        return store.getHistoricalRates();
    }
}