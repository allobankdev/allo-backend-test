package com.allobankdev.splitbill.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class PersonalizationServiceTest {

    private PersonalizationService service;

    @BeforeEach
    void setUp() {
        service = new PersonalizationService();
    }

    @Test
    void testCalculateServiceChargePct_johndoe47() {
        // johndoe47 -> 106+111+104+110+100+111+101+52+55 = 850
        // 850 % 10 = 0
        int pct = service.calculateServiceChargePct("johndoe47");
        assertEquals(0, pct);
    }

    @Test
    void testCalculateServiceChargePct_NekoSukuriputo() {
        // NekoSukuriputo -> nekosukuriputo
        // n=110, e=101, k=107, o=111, s=115, u=117, k=107, u=117, r=114, i=105, p=112, u=117, t=116, o=111
        // sum = 110+101+107+111+115+117+107+117+114+105+112+117+116+111 = 1560
        // 1560 % 10 = 0
        int pct = service.calculateServiceChargePct("NekoSukuriputo");
        assertEquals(0, pct);
    }
}
