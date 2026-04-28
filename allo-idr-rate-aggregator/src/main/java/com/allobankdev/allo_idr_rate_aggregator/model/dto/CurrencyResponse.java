package com.allobankdev.allo_idr_rate_aggregator.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CurrencyResponse {

    private String code;
    private String name;
    
}
