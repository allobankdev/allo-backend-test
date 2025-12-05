package achlaq.co.allo_backend_test.finance.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Map;

@Data
@AllArgsConstructor
public class LatestIdrRatesView {
    private LocalDate date;

    private String base;

    private Map<String, BigDecimal> rates;

    private BigDecimal spreadFactor;

    private BigDecimal usdBuySpreadIdr;
}
