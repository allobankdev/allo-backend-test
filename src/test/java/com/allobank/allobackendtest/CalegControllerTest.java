package com.allobank.allobackendtest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

import com.allobank.allobackendtest.controller.CalegController;
import com.allobank.allobackendtest.model.Caleg;
import com.allobank.allobackendtest.model.Dapil;
import com.allobank.allobackendtest.model.JenisKelamin;
import com.allobank.allobackendtest.model.Partai;
import com.allobank.allobackendtest.repository.CalegRepository;
import com.allobank.allobackendtest.repository.DapilRepository;
import com.allobank.allobackendtest.repository.PartaiRepository;
import com.allobank.allobackendtest.service.CalegService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.*;

public class CalegControllerTest {

    @Mock
    private CalegRepository calegRepository;

    @InjectMocks
    private CalegService calegService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetCaleg() {
        List<Caleg> calegList = new ArrayList<>();
        // Create Caleg data from JSON
        Caleg caleg = new Caleg();
        caleg.setId(UUID.fromString("f3122ae5-2115-4290-b6f7-8b0ea3acdb7d"));
        caleg.setNomorUrut(1);
        caleg.setNama("AsscProf. Dr. DARMADI DURIANTO");
        caleg.setJenisKelamin(JenisKelamin.LAKILAKI);

        // Create Dapil data
        Dapil dapil = new Dapil();
        dapil.setId(UUID.fromString("c31db712-62d8-4386-9b5c-0539c0b14aee"));
        dapil.setNamaDapil("DKI JAKARTA 3");
        dapil.setProvinsi("DKI JAKARTA");
        dapil.setWilayahDapilList(List.of("Kepulauan Seribu", "Jakarta Barat", "Jakarta Utara"));
        dapil.setJumlahKursi(8);

        // Create Partai data
        Partai partai = new Partai();
        partai.setId(UUID.fromString("3568e292-fe4b-44bb-b44a-0f1fba387046"));
        partai.setNamaPartai("PDIP");
        partai.setNomorUrut(1);

        // Set Dapil and Partai for the Caleg
        caleg.setDapil(dapil);
        caleg.setPartai(partai);
        calegList.add(caleg);
        // Mocking repository methods
        // Mocking repository methods
        when(calegRepository.findAllFiltered(anyString(), anyString(), anyBoolean())).thenReturn(calegList);

        // Test getCaleg method
        Map result = calegService.getCaleg("PDIP", "DKI JAKARTA 3", true);
        System.out.println(calegList);
        System.out.println(result);

        // Assertions
        assertEquals(calegList, result.get("data"));
    }

}

