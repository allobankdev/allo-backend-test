package achlaq.co.allo_backend_test.finance.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class HistoricalIdrUsdView {

    private Map<String, Map<String, BigDecimal>> rates;
}

