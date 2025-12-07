package id.tisnanda.allobank.allo_bank_backend_test.dto.strategy;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CurrenciesResponseDTO {
    private Map<String, String> currencies;
}
