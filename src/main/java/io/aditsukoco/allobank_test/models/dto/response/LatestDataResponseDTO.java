package io.aditsukoco.allobank_test.models.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.aditsukoco.allobank_test.models.dto.api_response.LatestAPIResponseDTO;
import lombok.Builder;
import lombok.Getter;

import java.util.Map;

@Getter
@Builder
public class LatestDataResponseDTO {

    @JsonProperty("amount")
    private float amount;

    @JsonProperty("base")
    private String base;

    @JsonProperty("date")
    private String date;

    @JsonProperty("rates")
    private Map<String, Float> rates;

    @JsonProperty("USD_BuySpread_IDR")
    private double result;

    public static LatestDataResponseDTO build(LatestAPIResponseDTO apiResponse, double buySpreadResult) {
        return LatestDataResponseDTO.builder()
                .amount(apiResponse.getAmount())
                .base(apiResponse.getBase())
                .date(apiResponse.getDate())
                .rates(apiResponse.getRates())
                .result(buySpreadResult)
                .build();
    }
}
