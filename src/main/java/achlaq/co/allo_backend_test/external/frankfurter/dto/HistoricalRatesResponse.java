package achlaq.co.allo_backend_test.external.frankfurter.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Map;

@Data
public class HistoricalRatesResponse {
    private BigDecimal amount;

    private String base;

    @JsonProperty("start_date")
    private LocalDate startDate;

    @JsonProperty("end_date")
    private LocalDate endDate;

    private Map<String, Map<String, BigDecimal>> rates;
}
