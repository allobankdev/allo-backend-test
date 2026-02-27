package com.amri.apiintegration.dto.frankfurter;

import java.math.BigDecimal;
import java.util.Map;

public record HistoricalRatesDto(BigDecimal amount, String base, Map<String, Map<String, BigDecimal>> rates) implements FinanceDataDto {
}
