package com.allobank.allobackendtest.services;

import com.allobank.allobackendtest.model.Caleg;
import com.allobank.allobackendtest.model.Dapil;
import com.allobank.allobackendtest.model.Partai;
import com.allobank.allobackendtest.repository.CalegRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

public class CalegSvcTest {
    @Mock
    private CalegRepository calegRepository;


    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetAllCalegAsDto() {
        // Mock entity data
        Partai partai = new Partai();
        partai.setSingkatanPartai("pde");
        partai.setNamaPartai("PEDE");
        partai.setId(1);

        Dapil dapil = new Dapil();
        dapil.setId(1);
        dapil.setNamaDapil("hendra");
        dapil.setWilayah("Depok");
        dapil.setAlokasiKursi(12);


        Caleg caleg = new Caleg();
        caleg.setId(1);
        caleg.setNamaCaleg("hendra");
        caleg.setNomorUrut(1);
        caleg.setPartai(partai);
        caleg.setDapil(dapil);

        Pageable pageable = (Pageable) PageRequest.of(0, 20);
        Page<Caleg> calegPage = new PageImpl<>(Collections.singletonList(caleg), pageable, 1);

        when(calegRepository.findByDapilNamaDapilContainingAndPartaiNamaPartaiContaining("hendra", "PEDE", pageable)).thenReturn(calegPage);

        assertEquals(1, calegPage.getTotalElements());
        assertEquals("hendra", calegPage.getContent().get(0).getNamaCaleg());
        assertEquals(1, calegPage.getContent().get(0).getNomorUrut());
    }
}


