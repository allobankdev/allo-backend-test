package com.allobank.finance.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;

import java.time.Instant;
import java.util.Map;

@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class FinanceDataResponse {

    private String resourceType;
    private String fetchedAt;
    private Object data;

    private Double usdBuySpreadIdr;
    private Double spreadFactor;
}
