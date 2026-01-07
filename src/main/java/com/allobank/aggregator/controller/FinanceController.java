package com.allobank.aggregator.controller;

import com.allobank.aggregator.service.FinanceDataStore;
import com.allobank.aggregator.dto.FinanceDataDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/finance/data")
public class FinanceController {

    private final FinanceDataStore store;

    public FinanceController(FinanceDataStore store) {
        this.store = store;
    }

    @GetMapping("/{resourceType}")
    public ResponseEntity<?> getByResourceType(@PathVariable("resourceType") String resourceType) {
        Optional<FinanceDataDto> data = store.get(resourceType);

        if (data.isEmpty()) {
            String allowed = String.join(", ", store.all().keySet());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Invalid resourceType. Allowed values: " + (allowed.isBlank() ? "latest_idr_rates, historical_idr_usd, supported_currencies" : allowed));
        }

        return ResponseEntity.ok(data.get());
    }

    private String[] getAllowedResourceTypes() {
        return store.all().keySet().stream().sorted().toArray(String[]::new);
    }
}
