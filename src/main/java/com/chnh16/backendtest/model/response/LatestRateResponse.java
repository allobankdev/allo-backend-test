package com.chnh16.backendtest.model.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Map;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class LatestRateResponse extends CommonResponse {

    private String date;
    private Map<String, Double> rates;
    private Double USDBuySpreadIDR;

    @JsonProperty(value = "USD_BuySpread_IDR")
    public Double getUSDBuySpreadIDR() {
        return this.USDBuySpreadIDR;
    }

}
