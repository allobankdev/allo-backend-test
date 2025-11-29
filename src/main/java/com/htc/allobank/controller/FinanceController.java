package com.htc.allobank.controller;

import com.htc.allobank.runner.FinanceDataHolder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/finance/data")
public class FinanceController {

    private final FinanceDataHolder holder;

    public FinanceController(FinanceDataHolder holder) {
        this.holder = holder;
    }

    @GetMapping("/{resourceType}")
    public ResponseEntity<?> getResource(@PathVariable String resourceType) {
        var store = holder.getStore();
        if (store == null) {
            return ResponseEntity.status(503).body("Data not loaded yet");
        }
        Object res = store.get(resourceType);
        if (res == null) {
            return ResponseEntity.badRequest().body("unknown resource type or failed to load");
        }
        return ResponseEntity.ok(res);
    }
}
