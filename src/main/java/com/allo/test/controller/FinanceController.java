package com.allo.test.controller;

import com.allo.test.service.strategy.IDRDataFetcher;
import com.allo.test.store.DataStore;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/finance")
@RequiredArgsConstructor
public class FinanceController {

    private final DataStore dataStore;
    private final List<IDRDataFetcher> fetchers;

    @GetMapping("/data/{type}")
    public ResponseEntity<?> getData(@PathVariable String type) {

        // validasi tanpa if/switch
        fetchers.stream()
                .filter(f -> f.getType().equals(type))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Invalid type: " + type));

        return ResponseEntity.ok(dataStore.get(type));
    }
}