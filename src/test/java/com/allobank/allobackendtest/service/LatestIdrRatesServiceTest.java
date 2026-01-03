package com.allobank.allobackendtest.service;

import java.math.BigDecimal;
import java.util.Map;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import static org.assertj.core.api.Assertions.assertThat;

import com.allobank.allobackendtest.model.DTO.LatestIdrRatesResponse;

import lombok.extern.slf4j.Slf4j;

@SpringBootTest
@Slf4j
class LatestIdrRatesServiceTest {

    private final LatestIdrRatesService service = new LatestIdrRatesService();

    @Test
    void shouldCalculateUsdBuySpreadCorrectly() {

        // given
        LatestIdrRatesResponse response = new LatestIdrRatesResponse();
        response.setBase("IDR");
        response.setRates(Map.of("USD", new BigDecimal("6.0E-05")));

        // when
        LatestIdrRatesResponse result = service.applyUsdBuySpread(response);

        log.info("USD Buy Spread IDR = {}", result.getUsdBuySpreadIdr());

        // then
        assertThat(result.getUsdBuySpreadIdr())
                .isNotNull()
                .isGreaterThan(new BigDecimal("15000"))
                .isLessThan(new BigDecimal("20000"));
    }

    @Test
    void shouldCalculateSpreadFactorConsistently() {

        BigDecimal spreadFactor = service.calculateSpreadFactor();

        assertThat(spreadFactor)
                .isGreaterThanOrEqualTo(BigDecimal.ZERO)
                .isLessThan(BigDecimal.valueOf(0.01));
    }

}
