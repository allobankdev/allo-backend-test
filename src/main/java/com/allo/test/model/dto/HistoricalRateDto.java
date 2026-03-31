package com.allo.test.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import java.math.BigDecimal;

@Data
@AllArgsConstructor
public class HistoricalRateDto {

    private String date;
    private BigDecimal rate;

}