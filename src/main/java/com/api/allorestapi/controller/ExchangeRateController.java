package com.api.allorestapi.controller;

import com.api.allorestapi.model.IdrSummaryResponse;
import com.api.allorestapi.service.ExchangeRateAggregatorService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@Slf4j
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class ExchangeRateController {

    private final ExchangeRateAggregatorService aggregatorService;

    @GetMapping(value = "/idr-summary", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<IdrSummaryResponse> getIdrSummary() {
        log.info("Received request for IDR summary");
        return aggregatorService.aggregateIdrData();
    }
}
