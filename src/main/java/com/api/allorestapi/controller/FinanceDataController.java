package com.api.allorestapi.controller;

import com.api.allorestapi.model.FinanceDataResponse;
import com.api.allorestapi.store.FinanceDataStore;
import com.api.allorestapi.strategy.IDRDataFetch;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/api/finance/data")
@Tag(name = "Finance Data", description = "Polymorphic IDR exchange rate endpoint powered by Frankfurter API")
public class FinanceDataController {

    private final FinanceDataStore store;
    private final Map<String, IDRDataFetch> fetcherMap;

    public FinanceDataController(List<IDRDataFetch> fetchers, FinanceDataStore store) {
        this.store = store;
        this.fetcherMap = fetchers.stream()
                .collect(Collectors.toMap(IDRDataFetch::getResourceType, Function.identity()));
        log.info("Controller registered {} strategies: {}", fetcherMap.size(), fetcherMap.keySet());
    }

    @Operation(
        summary = "Get IDR finance data by resource type",
        description = """
            Returns cached exchange rate data for the requested resource type. \
            Data is pre-loaded at startup from the Frankfurter API — no live HTTP call is made per request.
            
            **Available resource types:**
            - `latest_idr_rates` — Latest IDR exchange rates for all currencies + USD_BuySpread_IDR
            - `historical_idr_usd` — IDR → USD time-series from 2024-01-01 to 2024-01-05
            - `supported_currencies` — Full list of supported currency codes and names
            """
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "Successfully retrieved data",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = FinanceDataResponse.class),
                examples = {
                    @ExampleObject(
                        name = "latest_idr_rates",
                        summary = "Latest IDR Rates",
                        value = """
                            {
                              "resourceType": "latest_idr_rates",
                              "data": [{
                                "base": "IDR",
                                "date": "2025-02-26",
                                "rates": { "USD": 0.000062, "EUR": 0.000057 },
                                "USD_BuySpread_IDR": 16231.84,
                                "spreadFactor": 0.00637
                              }]
                            }
                            """
                    ),
                    @ExampleObject(
                        name = "historical_idr_usd",
                        summary = "Historical IDR to USD",
                        value = """
                            {
                              "resourceType": "historical_idr_usd",
                              "data": [
                                { "date": "2024-01-02", "rates": { "USD": 0.000064 } },
                                { "date": "2024-01-03", "rates": { "USD": 0.000064 } }
                              ]
                            }
                            """
                    ),
                    @ExampleObject(
                        name = "supported_currencies",
                        summary = "Supported Currencies",
                        value = """
                            {
                              "resourceType": "supported_currencies",
                              "data": [
                                { "code": "IDR", "name": "Indonesian Rupiah" },
                                { "code": "USD", "name": "US Dollar" }
                              ]
                            }
                            """
                    )
                }
            )
        ),
        @ApiResponse(responseCode = "400", description = "Invalid resourceType — must be one of: latest_idr_rates, historical_idr_usd, supported_currencies"),
        @ApiResponse(responseCode = "500", description = "Data not loaded correctly at startup")
    })
    @GetMapping(value = "/{resourceType}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<FinanceDataResponse> getData(
            @Parameter(
                description = "The type of financial data to retrieve",
                required = true,
                examples = {
                    @ExampleObject(name = "Latest Rates",     value = "latest_idr_rates"),
                    @ExampleObject(name = "Historical Rates", value = "historical_idr_usd"),
                    @ExampleObject(name = "Currencies",       value = "supported_currencies")
                }
            )
            @PathVariable String resourceType) {

        log.info("Request for resourceType='{}'", resourceType);

        if (!fetcherMap.containsKey(resourceType)) {
            log.warn("Unknown resourceType: '{}'", resourceType);
            return ResponseEntity.badRequest().build();
        }

        FinanceDataResponse cached = store.get(resourceType);
        if (cached == null) {
            log.error("Store miss for '{}'", resourceType);
            return ResponseEntity.internalServerError().build();
        }

        return ResponseEntity.ok(cached);
    }
}