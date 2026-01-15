package com.allobank.finance.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.allobank.finance.service.IDRService;

@RestController
@RequestMapping("/api/finance")
public class FinanceController {

    private final IDRService idrService;

    public FinanceController(IDRService idrService) {
        this.idrService = idrService;
    }

    @GetMapping("/data/{resourceType}")
    public ResponseEntity<Object> getFinanceData(@PathVariable String resourceType) {
        Object data = idrService.getData(resourceType);

        if (data == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(data);
    }
}
