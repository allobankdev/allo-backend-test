package id.allobank.exchangerate.model.dto;

import lombok.Data;

import java.util.Map;

@Data
public class LatestRatesResponse {
    private double amount;
    private String base;
    private String date;
    private Map<String, Double> rates;

    // custom field
    private Double USD_BuySpread_IDR;
}