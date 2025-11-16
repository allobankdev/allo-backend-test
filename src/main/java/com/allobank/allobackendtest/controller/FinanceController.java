package com.allobank.allobackendtest.controller;

import com.allobank.allobackendtest.service.InMemoryFinanceStore;
import com.allobank.allobackendtest.strategy.IdrDataFetcher;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/finance/data")
public class FinanceController {

    private final Map<String, IdrDataFetcher> strategiesByResource;
    private final InMemoryFinanceStore store;

    public FinanceController(Map<String, IdrDataFetcher> strategyBeans,
                             InMemoryFinanceStore store) {
        // Jadikan key = resourceType(), bukan dengan nama bean
        this.strategiesByResource = strategyBeans.values().stream()
                .collect(Collectors.toMap(
                        IdrDataFetcher::resourceType,
                        s -> s
                ));
        this.store = store;
    }

    @GetMapping("/{resourceType}")
    public ResponseEntity<List<Object>> getFinanceData(@PathVariable String resourceType) {
        if (!strategiesByResource.containsKey(resourceType)) {
            return ResponseEntity.notFound().build();
        }
        Object payload = store.getByResourceType(resourceType);
        // Requirement: unified JSON array of results
        return ResponseEntity.ok(List.of(payload));
    }
}
