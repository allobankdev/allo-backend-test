package achlaq.co.allo_backend_test.finance.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SupportedCurrenciesView {

    private Map<String, String> currencies;
}

