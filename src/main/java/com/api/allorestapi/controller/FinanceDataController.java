package com.api.allorestapi.controller;

import com.api.allorestapi.model.FinanceDataResponse;
import com.api.allorestapi.store.FinanceDataStore;
import com.api.allorestapi.strategy.IDRDataFetch;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/api/finance/data")
public class FinanceDataController {

    private final FinanceDataStore store;

    private final Map<String, IDRDataFetch> fetcherMap;

    public FinanceDataController(List<IDRDataFetch> fetchers, FinanceDataStore store) {
        this.store = store;
        this.fetcherMap = fetchers.stream()
                .collect(Collectors.toMap(IDRDataFetch::getResourceType, Function.identity()));
        log.info("Controller registered {} strategies: {}", fetcherMap.size(), fetcherMap.keySet());
    }

    @GetMapping(value = "/{resourceType}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<FinanceDataResponse> getData(@PathVariable String resourceType) {
        log.info("Request for resourceType='{}'", resourceType);

        if (!fetcherMap.containsKey(resourceType)) {
            log.warn("Unknown resourceType: '{}'", resourceType);
            return ResponseEntity.badRequest().build();
        }

        FinanceDataResponse cached = store.get(resourceType);
        if (cached == null) {
            log.error("Store miss for '{}' — data may not have loaded", resourceType);
            return ResponseEntity.internalServerError().build();
        }

        return ResponseEntity.ok(cached);
    }
}
