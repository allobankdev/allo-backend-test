package com.allo.test.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class HistoricalRateDto {

    private String date;
    private Double rate;

}