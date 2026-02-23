package com.allobank.finance.idrrateaggregator.controller;

import com.allobank.finance.idrrateaggregator.model.dto.FinanceResponse;
import com.allobank.finance.idrrateaggregator.service.FinanceDataStore;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/finance/data")
public class FinanceController {

    private final FinanceDataStore store;

    public FinanceController(FinanceDataStore store) {
        this.store = store;
    }

    @GetMapping("/{resourceType}")
    public List<FinanceResponse> getData(@PathVariable String resourceType) {
        return store.get(resourceType);
    }

}
