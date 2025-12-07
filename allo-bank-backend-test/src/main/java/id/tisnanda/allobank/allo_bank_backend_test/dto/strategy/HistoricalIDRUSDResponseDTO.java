package id.tisnanda.allobank.allo_bank_backend_test.dto.strategy;


import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class HistoricalIDRUSDResponseDTO {
    private String date;

    @JsonProperty("USD")
    private Double usd;
}
