package com.finance.exchange.controller;

import com.finance.exchange.strategy.IDRDataFetcher;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@RestController
@RequestMapping("/api/finance")
public class FinanceController {

  private final Map<String, IDRDataFetcher> strategyMap = new ConcurrentHashMap<>();

  public FinanceController(List<IDRDataFetcher> strategies) {
    for (IDRDataFetcher strategy : strategies) {
      String key = strategy.getResourceType();
      if (key != null) {
        this.strategyMap.put(key, strategy);
      }
    }
  }

  @GetMapping("/data/{resourceType}")
  public ResponseEntity<?> getFinanceData(@PathVariable String resourceType) {
    IDRDataFetcher strategy = strategyMap.get(resourceType);

    if (strategy == null) {
      return ResponseEntity.badRequest()
          .body(Map.of("error", "Invalid resource type."));
    }

    Object data = strategy.getData();
    if (data == null) {
      return ResponseEntity.status(503)
          .body(Map.of("error", "Data not ready or external API failed."));
    }

    return ResponseEntity.ok(data);
  }
}