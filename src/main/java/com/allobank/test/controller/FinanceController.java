package com.allobank.test.controller;

import com.allobank.test.service.ExchangeRateStore;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/finance")
@RequiredArgsConstructor
public class FinanceController {

  private final ExchangeRateStore store;

  @GetMapping("/data/{resourceType}")
  public ResponseEntity<Object> getFinanceData(@PathVariable String resourceType) {
    Object data = store.getData(resourceType);
    return data != null ? ResponseEntity.ok(data) : ResponseEntity.notFound().build();
  }
}