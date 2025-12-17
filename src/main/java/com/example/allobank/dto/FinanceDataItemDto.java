package com.example.allobank.dto;

import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Unified response item for all resource types.
 * Endpoint MUST return an array -> List<FinanceDataItemDto>.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FinanceDataItemDto {
    private String resourceType; // latest_idr_rates | historical_idr_usd | supported_currencies
    private String key;          // currency code OR date OR computed key
    private Object value;        // BigDecimal rate OR String name, etc.
    private Map<String, Object> meta; // optional metadata (date, base, spreadFactor, etc.)
}