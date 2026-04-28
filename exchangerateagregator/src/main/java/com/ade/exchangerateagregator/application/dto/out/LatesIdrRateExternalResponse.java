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
public class LatesIdrRateExternalResponse implements Serializable {
    private static final long serialVersionUID= 8877469297830350369L;
    private BigDecimal amount;
    @JsonProperty("base")
    private String baseCurrency;
    private String date;
    private Map<String, BigDecimal> rates = new HashMap<>();
}
