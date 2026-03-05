package com.ade.exchangerateagregator.application.dto.out;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SupportedCurrencyResponse implements Serializable {
    private static final long serialVersionUID= 5945900847867938512L;
    private Map<String, String> currencies = new HashMap<>();
}
