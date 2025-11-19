package com.allobank.controller;

import com.allobank.service.FinanceService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/finance/data")
public class FinanceController {

    private static final Logger log = LoggerFactory.getLogger(FinanceController.class);

    private final FinanceService service;

    public FinanceController(FinanceService service) {
        this.service = service;
    }

    @GetMapping("/{type}")
    public ResponseEntity<?> get(@PathVariable String type) {
        log.info("Request received: GET /api/finance/data/{}", type);
        try {
            Object data = service.getData(type);
            log.debug("Returning {} data (size: {})", type, data.toString().length());
            return ResponseEntity.ok(data);
        } catch (IllegalArgumentException e) {
            log.warn("Invalid resource type requested: {}", type);
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}