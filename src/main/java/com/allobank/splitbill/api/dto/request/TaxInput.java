package com.allobank.splitbill.api.dto.request;

import com.allobank.splitbill.domain.TaxMode;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record TaxInput(
        @NotNull TaxMode mode,
        @DecimalMin("0.00") @DecimalMax("100.00") @Digits(integer = 3, fraction = 2)
        BigDecimal ratePct
) {
}
