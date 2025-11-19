package com.allo.test.modules.finance.dto.res;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * Response DTO for a single historical exchange rate entry.
 * <p>
 * Represents one rate data point with date, currency, and rate value.
 * Used in list format for historical rates API response.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HistoricalRateResponse {

    /**
     * The date of the exchange rate in ISO 8601 format (YYYY-MM-DD)
     */
    private String date;

    /**
     * The currency code (e.g., "USD", "EUR")
     */
    private String currency;

    /**
     * The exchange rate value
     */
    private BigDecimal rate;
}
