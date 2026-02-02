package com.example.allotest.controller;

import com.example.allotest.constants.ApiResponseConstants;
import com.example.allotest.dtos.BaseResponseDto;
import com.example.allotest.exceptions.NoPathAvailableException;
import com.example.allotest.store.DataStore;
import com.example.allotest.strategy.IDataFetcher;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class ApiClientController {
    private final DataStore store;
    private final Map<String, IDataFetcher> strategies;

    public ApiClientController(DataStore store,
                               Map<String, IDataFetcher> strategies) {
        this.store = store;
        this.strategies = strategies;
    }

    @GetMapping("/api/finance/data/{resourceType}")
    public ResponseEntity<BaseResponseDto<Object[]>> getData(@PathVariable String resourceType) {
        if (strategies.get(resourceType) == null) {
            throw new NoPathAvailableException("unknown path: " + resourceType, resourceType);
        }

        Object[] data = store.getFromStore(resourceType);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new BaseResponseDto<>(
                        ApiResponseConstants.OK_STATUS_CODE,
                        ApiResponseConstants.OK_STATUS_MESSAGE,
                        resourceType,
                        data));
    }
}
