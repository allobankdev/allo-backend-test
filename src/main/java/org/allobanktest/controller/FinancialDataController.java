package org.allobanktest.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.allobanktest.common.exception.InvalidResourceTypeException;
import org.allobanktest.common.helpers.BaseResponse;
import org.allobanktest.common.helpers.ResponseWrapper;
import org.allobanktest.store.FinancialDataStore;
import org.allobanktest.strategy.IDRDataFetcher;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/finance/data")
@Slf4j
@RequiredArgsConstructor
public class FinancialDataController {
    private final FinancialDataStore store;
    private final Map<String, IDRDataFetcher> strategies;

    @GetMapping(value = "/{resourceType}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<BaseResponse<List<?>>> getFinancialData(
            @PathVariable String resourceType
    ) {
        IDRDataFetcher strategy = strategies.get(resourceType);

        if (strategy == null) {
            throw new InvalidResourceTypeException(resourceType);
        }

        List<?> data = strategy.getCached(store);

        return ResponseEntity.ok(
                ResponseWrapper.success(data)
        );
    }
}
