package com.allobank.splitbill.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;

class PersonalizationServiceTest {

    @Test
    @DisplayName("Should calculate service_charge_pct = 5 for GitHub username 'resa-rm'")
    void testCalculateServiceChargePctForResaRm() {
        // ASCII values: r(114) + e(101) + s(115) + a(97) + -(45) + r(114) + m(109) = 695
        // 695 % 10 = 5
        PersonalizationService service = new PersonalizationService("resa-rm");
        int pct = service.calculateServiceChargePct();
        assertEquals(5, pct, "Expected service charge percentage for 'resa-rm' to be 5%");
    }

    @Test
    @DisplayName("Should calculate service_charge_amount correctly for total expenses")
    void testCalculateServiceChargeAmount() {
        PersonalizationService service = new PersonalizationService("resa-rm");
        BigDecimal totalExpenses = new BigDecimal("450.00");
        // 5% of 450.00 = 22.50
        BigDecimal amount = service.calculateServiceChargeAmount(totalExpenses);
        assertEquals(new BigDecimal("22.50"), amount);
    }
}
