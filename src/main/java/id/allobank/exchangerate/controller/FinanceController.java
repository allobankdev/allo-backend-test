package id.allobank.exchangerate.controller;

import id.allobank.exchangerate.service.FinanceService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/finance/data")
@RequiredArgsConstructor
@Slf4j
public class FinanceController {
    private final FinanceService service;

    @GetMapping("/{type}")
    public ResponseEntity<?> get(@PathVariable String type) {
        log.info("Request: {}", type);
        return ResponseEntity.ok(service.getData(type));
    }}
