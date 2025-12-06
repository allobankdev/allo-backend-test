package achlaq.co.allo_backend_test.finance.util;

import achlaq.co.allo_backend_test.common.util.SpreadFactorCalculator;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

class SpreadFactorCalculatorTest {

    @Test
    void calculateSpreadFactor_example() {
        BigDecimal factor = SpreadFactorCalculator.calculateSpreadFactor("achlaq");
        assertThat(factor).isNotNull();
        assertThat(factor).isGreaterThanOrEqualTo(BigDecimal.ZERO);
        assertThat(factor.scale()).isLessThanOrEqualTo(5);
    }

    @Test
    void calculateUsdBuySpreadIdr_example() {
        BigDecimal rateUsd = new BigDecimal("0.0000641234");
        BigDecimal spreadFactor = SpreadFactorCalculator.calculateSpreadFactor("achlaq");
        BigDecimal result = SpreadFactorCalculator.calculateUsdBuySpreadIdr(rateUsd, spreadFactor);
        assertThat(result).isNotNull();
        assertThat(result).isGreaterThan(BigDecimal.ZERO);
    }
}
