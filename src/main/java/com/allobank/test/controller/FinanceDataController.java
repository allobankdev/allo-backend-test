package com.allobank.test.controller;

import com.allobank.test.service.FinanceDataService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/finance/data")
@Tag(name = "Finance Data", description = "Endpoints for preloaded finance data resources")
public class FinanceDataController {

    private final FinanceDataService financeDataService;

    public FinanceDataController(FinanceDataService financeDataService) {
        this.financeDataService = financeDataService;
    }

    @GetMapping("/{resourceType}")
    @Operation(
            summary = "Get finance data by resource type",
            description = "Supported resourceType: latest_idr_rates, historical_idr_usd, supported_currencies. Use other values to test invalid response (400)."
    )
    @ApiResponse(responseCode = "200", description = "Resource found")
    @ApiResponse(responseCode = "400", description = "Unsupported resource type")
    public Map<String, Object> getFinanceData(
            @Parameter(description = "Type of finance resource. Examples: latest_idr_rates, historical_idr_usd, supported_currencies, invalid_type")
            @PathVariable String resourceType
    ) {
        return Map.of(
                "resourceType", resourceType,
                "data", financeDataService.findByResourceType(resourceType)
        );
    }
}
