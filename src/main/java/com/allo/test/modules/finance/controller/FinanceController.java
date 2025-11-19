package com.allo.test.modules.finance.controller;

import com.allo.test.modules.finance.service.ExchangeRateService;
import com.allo.test.shared.response.template.ResponseData;
import com.allo.test.shared.response.ResponseEnum;
import com.allo.test.shared.response.ResponseHelper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * REST controller for accessing IDR exchange rate data.
 * <p>
 * Provides a single endpoint that serves three different resource types:
 * - latest_idr_rates: Latest rates with USD buy spread calculation
 * - historical_idr_usd: Historical IDR to USD rates
 * - supported_currencies: List of all supported currencies
 * <p>
 * All data is served from an in-memory store populated at application startup.
 * Routing logic is delegated to the service layer.
 */
@Slf4j
@RestController
@RequestMapping("/api/finance")
@RequiredArgsConstructor
public class FinanceController {

    private final ExchangeRateService exchangeRateService;
    private final ResponseHelper responseHelper;

    /**
     * Retrieves exchange rate data for the specified resource type.
     * <p>
     * Endpoint: GET /api/finance/data/{resourceType}
     * <p>
     * Valid resource types:
     * <ul>
     *   <li>latest_idr_rates - Latest exchange rates with USD buy spread</li>
     *   <li>historical_idr_usd - Historical IDR to USD rates (2024-01-01 to 2024-01-05)</li>
     *   <li>supported_currencies - List of all supported currency symbols</li>
     * </ul>
     *
     * @param resourceType the type of resource to retrieve
     * @return ResponseEntity containing the requested data or error response
 */
    @GetMapping("/data/{resourceType}")
    public ResponseEntity<ResponseData<Object>> getData(@PathVariable String resourceType) {
        return responseHelper.createResponseData(
                ResponseEnum.SUCCESS,
                exchangeRateService.getData(resourceType)
        );
    }

}
