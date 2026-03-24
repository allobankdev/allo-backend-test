package com.allobank.test.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class HomeController {

    @GetMapping("/")
    public Map<String, Object> home() {
        return Map.of(
                "app", "allo-backend-test",
                "message", "Service is running",
                "endpoints", Map.of(
                        "finance_data", "/api/finance/data/{resourceType}"));
    }
}
