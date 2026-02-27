package com.allobank.finance.controller;

import com.allobank.finance.enums.ResourceType;
import com.allobank.finance.store.FinanceDataStore;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/finance/data")
@RequiredArgsConstructor
public class FinanceController {

    private final FinanceDataStore financeDataStore;

    @GetMapping("/{resourceType}")
    public Object getData(@PathVariable String resourceType) {
        ResourceType type = ResourceType.from(resourceType);
        return financeDataStore.getData(type.getValue());
    }
}