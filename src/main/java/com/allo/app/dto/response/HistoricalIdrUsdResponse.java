package com.allo.app.dto.response;

import java.util.Map;

import com.allo.app.dto.AmountBaseResponse;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class HistoricalIdrUsdResponse extends AmountBaseResponse {

    @JsonProperty("start_date")
    private String startDate;

    @JsonProperty("end_date")
    private String endDate;

    private Map<Object, Object> rates; 
}
