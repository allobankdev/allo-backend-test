package com.allo.test.modules.finance.dto.res;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Response DTO for a single currency entry.
 * <p>
 * Represents a currency with its code and full name.
 * Used in list format for supported currencies API response.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CurrencyResponse {

    /**
     * The currency code (e.g., "USD", "EUR", "IDR")
     */
    private String code;

    /**
     * The full name of the currency (e.g., "United States Dollar")
     */
    private String name;
}
