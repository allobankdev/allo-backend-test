package com.allobank.allobackend.web;

import com.allobank.allobackend.core.domain.FinanceDataStore;
import com.allobank.allobackend.core.dto.ResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/finance/data")
@RequiredArgsConstructor
@Tag(name = "Exchange Rate API" , description = "Endpoint for Exchange Rate API")
public class FinanceController {

    private final FinanceDataStore store;

    @GetMapping(value = {"/{resourceType}"})
    @Operation(
            summary = "Get aggregated IDR exchange rate data",
            description = """
                 Retrieves currency exchange rate data with IDR (Indonesian Rupiah) as the base currency.
                    
                                    **Available Resource Types:**
                                    1. `latest_idr_rates`: Displays the most recent exchange rates. If the 'target' parameter is provided, it returns the value inclusive of the calculated spread factor.
                                    2. `historical_idr_usd`: Displays the historical exchange rate of IDR against USD. This automatically retrieves data for the **last 5 days** from today.
                                    3. `supported_currencies`: Displays a list of all currencies supported by the system.
                    
                                    **How to Use:**
                                    - Provide the `resourceType` from the list above as a path variable.
                                    - Use the `target` query parameter (e.g., `USD`, `SGD`) to get the calculated spread for the 'latest' type.
                                    - For the 'historical' type, data is displayed as-is based on the 5-day history.
                """
    )
    @ApiResponse(responseCode = "200", description = "Data successfully retrieved")
    @ApiResponse(responseCode = "404", description = "Resource type not found or not yet initialized")
    public ResponseEntity<ResponseDto> getRate(
            @Parameter(description = "Type Data", example = "latest_idr_rates")
            @PathVariable String resourceType ,
            @Parameter(description = "Mata uang target (opsional), contoh: USD, EUR")
            @RequestParam(name = "target", required = false, defaultValue = "") String targetCurrency
            ){

        Object data = store.get(resourceType);

        if(data == null){
            return ResponseEntity.status(404).body(
                    ResponseDto.builder().message("Resource not found").build());
        }

        if(targetCurrency == null || targetCurrency.isEmpty()){
            Map<String, Object> mapData = (Map<String, Object>) data;
            Object rawData  = mapData.get("datas");
            return ResponseEntity.ok(ResponseDto.builder()
                    .base("IDR")
                    .rate(rawData)
                    .message("Success")
                    .build());
        }

        if (data instanceof Map) {
            Map<String, Object> mapData = (Map<String, Object>) data;
            Object rawData = mapData.get("datas");

            if(rawData instanceof Map<?,?> innerMap){
                if(innerMap.containsKey("rates") && innerMap.get("rates") instanceof Map<? , ?> historyMap){
                    Map<String , Object>  historyObject = new HashMap<>();

                    historyMap.forEach((date , currencyData) -> {
                        if(currencyData instanceof  Map<? , ?> rates){
                            Object val = rates.get(targetCurrency.toUpperCase());

                            if(val != null){

                                historyObject.put((String) date , Map.of(targetCurrency.toUpperCase() , val));
                            }
                        }
                    });

                    return ResponseEntity.ok(ResponseDto.builder()
                            .base("IDR")
                            .target(targetCurrency)
                            .rate(historyObject)
                            .message("Success")
                            .build());
                }

                String searchKey = targetCurrency.toUpperCase() + "_BuySpread_IDR";
                Object caculateVal = innerMap.get(searchKey);
                if(caculateVal == null){
                    caculateVal = innerMap.get(targetCurrency.toUpperCase());
                }

                if(caculateVal != null){
                    return ResponseEntity.ok(ResponseDto.builder()
                            .base("IDR")
                            .target(targetCurrency)
                            .rate(Map.of(searchKey , caculateVal))
                            .message("Success")
                            .build());
                }

            }

        }


        return ResponseEntity.badRequest().body(
                ResponseDto.builder().message("Currency " + targetCurrency + " not supported").build()
        );

    }

}
