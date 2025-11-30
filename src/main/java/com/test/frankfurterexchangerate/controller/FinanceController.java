package com.test.frankfurterexchangerate.controller;

import com.test.frankfurterexchangerate.service.FinanceDataStore;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/finance/data")
@CrossOrigin(origins = "*")
public class FinanceController {

    private final FinanceDataStore store;

    public FinanceController(FinanceDataStore store) {
        this.store = store;
    }

    @GetMapping("/{resourceType}")
    public List<Object> getData(@PathVariable String resourceType){
        return store.get(resourceType);
    }
}
