package com.allobank.finance.controller;

import com.allobank.finance.service.InMemoryFinanceStore;
import com.allobank.finance.strategy.IDRDataFetcher;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/finance/data")
public class FinanceController {

    private final InMemoryFinanceStore inMemoryFinanceStore;
    private final Map<String, IDRDataFetcher> strMap;

    public FinanceController(InMemoryFinanceStore inMemoryFinanceStore, List<IDRDataFetcher> fetchers) {
        this.inMemoryFinanceStore = inMemoryFinanceStore;
        this.strMap = fetchers.stream().collect(Collectors.toMap(IDRDataFetcher::getResourceType,
                Function.identity()));
    }

    @GetMapping("/{resourceType}")
    public ResponseEntity<?> getData(@PathVariable String resourceType){

        if(!strMap.containsKey(resourceType)){
            return ResponseEntity.badRequest().build();
        }

        Object data = inMemoryFinanceStore.getData(resourceType);

        return ResponseEntity.ok(data);
    }
}
