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
public class LatestAPIResponseDTO {

    @JsonProperty("amount")
    private float amount;

    @JsonProperty("base")
    private String base;

    @JsonProperty("date")
    private String date;

    @JsonProperty("rates")
    private Map<String, Float> rates;

}
