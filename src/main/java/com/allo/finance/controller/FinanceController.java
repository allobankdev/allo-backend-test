package com.allo.finance.controller;

import com.allo.finance.store.DataStore;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/finance")
public class FinanceController {

    private final DataStore store;

    public FinanceController(DataStore store){
        this.store = store;
    }

    @GetMapping("/data/{type}")
    public ResponseEntity<?> get(@PathVariable String type){
        return ResponseEntity.ok(store.get(type));
    }
}