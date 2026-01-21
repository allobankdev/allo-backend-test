package com.allo.app.dto.response;

import java.math.BigDecimal;
import java.util.Map;

import com.allo.app.dto.AmountBaseResponse;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LastestIdrRatesResponse extends AmountBaseResponse {

    private String date;

    private BigDecimal USDBuySpreadIDR;

    private Map<String, Object> rates;
    
}
