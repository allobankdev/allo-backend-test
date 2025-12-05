package achlaq.co.allo_backend_test.external.frankfurter.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Map;

@Data
public class LatestRatesResponse {
    private BigDecimal amount;

    private String base;

    private LocalDate date;

    private Map<String, BigDecimal> rates;
}
