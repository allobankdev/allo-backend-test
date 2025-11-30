package com.test.frankfurterexchangerate.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FrankfurterLatestDto {
    private int amount;
    private String base;
    private String date;
    private Map<String, Double> rates;
}
