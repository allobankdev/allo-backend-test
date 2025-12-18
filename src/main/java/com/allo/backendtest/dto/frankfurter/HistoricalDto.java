package com.allo.backendtest.dto.frankfurter;

import com.fasterxml.jackson.annotation.JsonInclude;
import tools.jackson.databind.PropertyNamingStrategies;
import tools.jackson.databind.annotation.JsonNaming;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record HistoricalDto(String message,
                            BigDecimal amount,
                            String base,
                            LocalDate startDate,
                            LocalDate endDate,
                            Map<LocalDate,Map<String, BigDecimal>> rates) {

}
