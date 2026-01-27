package com.interview.backend.controllers;

import com.interview.backend.services.FinanceDataStore;
import com.interview.backend.strategy.IDRDataFetcher;
import com.interview.backend.utils.ResponseUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/api/finance")
public class Finance {

    private final FinanceDataStore dataStore;
    private final Map<String, IDRDataFetcher> strategyMap;

    public Finance(FinanceDataStore dataStore, List<IDRDataFetcher> dataFetchers) {
        this.dataStore = dataStore;
        this.strategyMap = dataFetchers.stream()
                .collect(Collectors.toMap(
                        IDRDataFetcher::getResourceType,
                        fetcher -> fetcher));
    }

    @GetMapping("/data/{resourceType}")
    public ResponseEntity<Map<String, Object>> getData(
            @PathVariable String resourceType,
            @RequestParam(required = false, defaultValue = "defaultuser") String githubUsername,
            @RequestParam(value = "start_date", required = false) String startDate,
            @RequestParam(value = "end_date", required = false) String endDate) {
        try {

            if (!strategyMap.containsKey(resourceType)) {
                return ResponseUtil.error("Invalid resource type: " + resourceType +
                        ". Valid types are: " + strategyMap.keySet());
            }

            Map<String, Object> data;

            if ("historical_idr_usd".equals(resourceType) && (startDate != null || endDate != null)) {
                Map<String, String> params = new java.util.HashMap<>();
                if (startDate != null) {
                    params.put("start_date", startDate);
                }
                if (endDate != null) {
                    params.put("end_date", endDate);
                }
                data = strategyMap.get(resourceType).fetchData(params);
            } else {
                data = dataStore.getData(resourceType, githubUsername);
            }

            if (data == null) {
                return ResponseUtil.error("Data not available for resource type: " + resourceType);
            }

            return ResponseUtil.success(data, "Data retrieved successfully");

        } catch (Exception e) {
            return ResponseUtil.internalError("Error retrieving data: " + e.getMessage());
        }
    }

    @GetMapping("/health")
    public ResponseEntity<Map<String, Object>> health() {
        Map<String, Object> health = Map.of(
                "initialized", dataStore.isInitialized(),
                "availableResources", dataStore.getAvailableResourceTypes());
        return ResponseUtil.success(health, "Health check");
    }
}
