package com.vii.idragregator.dto;

import lombok.Data;

import java.util.Map;

@Data
public class LatestRateDTO {
    private String base;
    private String date;
    private Map<String, Double> rates;
    private Double usd_buySpread_idr;
}
