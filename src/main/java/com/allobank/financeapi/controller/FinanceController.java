package com.allobank.financeapi.controller;

import com.allobank.financeapi.model.FinanceData;
import com.allobank.financeapi.service.FinanceService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/finance/data")
@RequiredArgsConstructor
public class FinanceController {

    private final FinanceService financeService;

    @GetMapping("/{resourceType}")
    public Mono<ResponseEntity<FinanceData>> getData(@PathVariable String resourceType) {
        return financeService.getDataFromStore(resourceType)
                .map(ResponseEntity::ok)
                .onErrorReturn(ResponseEntity.badRequest().build());
    }
}