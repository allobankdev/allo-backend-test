package com.allobank.splitbill.domain;

import java.math.BigDecimal;
import java.math.RoundingMode;

public final class TaxCalculator {
    private static final BigDecimal HUNDRED = new BigDecimal("100.00");
    private static final BigDecimal ZERO = new BigDecimal("0.00");

    public record Result(BigDecimal inputAmount, TaxMode mode, BigDecimal ratePct,
                         BigDecimal subtotal, BigDecimal taxAmount, BigDecimal totalAmount) {}

    public Result calculate(BigDecimal inputAmount, TaxMode mode, BigDecimal ratePct) {
        if (inputAmount == null || inputAmount.signum() <= 0 || inputAmount.scale() > 2) {
            throw new IllegalArgumentException("amount must be positive with at most two decimal places");
        }
        if (mode == null) {
            throw new IllegalArgumentException("tax.mode is required when tax is provided");
        }

        BigDecimal normalizedInput = inputAmount.setScale(2, RoundingMode.UNNECESSARY);
        if (mode == TaxMode.NONE) {
            if (ratePct != null && ratePct.signum() != 0) {
                throw new IllegalArgumentException("tax.rate_pct must be omitted or zero when tax.mode is NONE");
            }
            return new Result(normalizedInput, mode, ZERO, normalizedInput, ZERO, normalizedInput);
        }

        if (ratePct == null || ratePct.signum() <= 0 || ratePct.compareTo(HUNDRED) > 0
                || ratePct.scale() > 2) {
            throw new IllegalArgumentException("tax.rate_pct must be greater than 0 and at most 100 with two decimal places");
        }
        BigDecimal normalizedRate = ratePct.setScale(2, RoundingMode.UNNECESSARY);
        if (mode == TaxMode.EXCLUSIVE) {
            BigDecimal taxAmount = normalizedInput.multiply(normalizedRate)
                    .divide(HUNDRED, 2, RoundingMode.HALF_UP);
            return new Result(normalizedInput, mode, normalizedRate, normalizedInput, taxAmount,
                    normalizedInput.add(taxAmount));
        }

        BigDecimal taxAmount = normalizedInput.multiply(normalizedRate)
                .divide(HUNDRED.add(normalizedRate), 2, RoundingMode.HALF_UP);
        return new Result(normalizedInput, mode, normalizedRate, normalizedInput.subtract(taxAmount),
                taxAmount, normalizedInput);
    }
}
