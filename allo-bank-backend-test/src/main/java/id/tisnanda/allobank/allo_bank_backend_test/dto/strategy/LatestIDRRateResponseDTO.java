package id.tisnanda.allobank.allo_bank_backend_test.dto.strategy;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LatestIDRRateResponseDTO {
    private String date;

    @JsonProperty("USD")

    private Double usd;

    @JsonProperty("USD_BuySpread_IDR")
    private Double usdBuySpreedIDR;
}
