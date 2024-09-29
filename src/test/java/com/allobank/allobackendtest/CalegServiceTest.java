package com.allobank.allobackendtest;

import com.allobank.allobackendtest.model.Caleg;
import com.allobank.allobackendtest.service.CalegService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class CalegServiceTest {

    @Autowired
    CalegService calegService;

    @Test
    void getAllCalegSuccessTest(){

        Page<Caleg> allCaleg = calegService.getAllCaleg("Dapil 1", "Partai A", "nomorUrut", 0, 10);

        assertNotNull(allCaleg.getContent());
        assertEquals(allCaleg.getContent().get(0).getDapil().getNamaDapil(), "Dapil 1");
        assertTrue(allCaleg.getContent().get(0).getNomorUrut()>allCaleg.getContent().get(allCaleg.getSize()-1).getNomorUrut());
    }

    @Test
    void getAllCalegFailedTest(){

        Page<Caleg> allCaleg = calegService.getAllCaleg("Dapil 1", "Partai A", "nomorUrut", 0, 10);

        assertNotEquals(allCaleg.getContent().get(0).getDapil().getNamaDapil(), "Dapil 2");
        assertFalse(allCaleg.getContent().get(0).getNomorUrut()<allCaleg.getContent().get(allCaleg.getSize()-1).getNomorUrut());
    }
}