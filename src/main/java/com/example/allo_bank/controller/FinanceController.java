package com.example.allo_bank.controller;

import com.example.allo_bank.dto.ApiResponse;
import com.example.allo_bank.util.TypeEnum;
import com.example.allo_bank.service.GetStorageDataStrategyService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/finance/data")
public class FinanceController {

    Logger log = LoggerFactory.getLogger(FinanceController.class);

    @Autowired
    private GetStorageDataStrategyService getStorageDataStrategyService;

    @GetMapping("/{resourceType}")
    public ResponseEntity<ApiResponse<Object>> getData(@PathVariable("resourceType") TypeEnum resourceType) {

        return ResponseEntity.ok(getStorageDataStrategyService.getData(resourceType));

    }

}
