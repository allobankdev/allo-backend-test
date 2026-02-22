package io.aditsukoco.allobank_test.models.dto.api_response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Map;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class HistoricalDataAPIResponseDTO {

    @JsonProperty("amount")
    private float amount;

    @JsonProperty("base")
    private String base;

    @JsonProperty("start_date")
    private String startDate;

    @JsonProperty("end_date")
    private String endDate;

    @JsonProperty("rates")
    private Map<String, Map<String, Float>> rates;

}
