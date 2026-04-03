package com.allo.finance.controller;

import com.allo.finance.dto.ApiResponse;
import com.allo.finance.store.DataStore;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/finance/data")
public class FinanceController {

    private final DataStore dataStore;

    public FinanceController(DataStore dataStore) {
        this.dataStore = dataStore;
    }

    @GetMapping("/{type}")
    public List<ApiResponse> get(@PathVariable String type) {

        Object data = dataStore.get(type);

        return List.of(new ApiResponse(type, data));
    }
}