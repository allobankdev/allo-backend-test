package com.zultest.allobank_backend_test.controller;

import com.zultest.allobank_backend_test.store.InMemoryStore;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/finance/data")
public class FinancialDataController {
    private final InMemoryStore store;

    public FinancialDataController(InMemoryStore store) {
        this.store = store;
    }

    @GetMapping("/{resourceType}")
    public ResponseEntity<List<?>> getFinanceData(@PathVariable String resourceType) {
        Object data = store.get(resourceType);
        return ResponseEntity.ok((data instanceof List<?>) ? (List<?>) data : List.of(data));
    }
}
