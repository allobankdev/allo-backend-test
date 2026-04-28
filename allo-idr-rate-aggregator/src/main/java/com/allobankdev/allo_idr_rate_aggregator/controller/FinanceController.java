package com.allobankdev.allo_idr_rate_aggregator.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.allobankdev.allo_idr_rate_aggregator.service.FinanceDataStore;

import reactor.core.publisher.Mono;

import java.util.List;

@RestController
@RequestMapping("/api/finance/data")
@RequiredArgsConstructor
public class FinanceController {

    private final FinanceDataStore store;

    @GetMapping("/{resourceType}")
    public Mono<List<Object>> getData(
            @PathVariable String resourceType) {
        return store.get(resourceType);
    }
}

