package com.ade.exchangerateagregator.application.dto.out;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class HistorycalResponse implements Serializable {
    private static final long serialVersionUID= 3046664962933683459L;
    private BigDecimal amount;
    @JsonProperty("base")
    private String baseCurrency;
    @JsonProperty("start_date")
    private String startDate;
    @JsonProperty("end_date")
    private String endDate;
    private Map<String,Map<String,BigDecimal>> rates = new HashMap<>();
}
