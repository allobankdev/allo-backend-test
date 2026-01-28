package com.example.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LatestResult {

    private LatestResponse originalData;
    private double usdBuySpreadIdr;
}
