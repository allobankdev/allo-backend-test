package com.allobank.allobackendtest.service;

import com.allobank.allobackendtest.model.Caleg;
import com.allobank.allobackendtest.model.enums.JenisKelamin;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

import java.util.List;

public class CalegServiceTest {

    private CalegService calegService;

    @Before
    public void setUp() {
        calegService = new CalegService(); // Pastikan CalegService diinisialisasi dengan data dummy
    }

    @Test
    public void testGetAllCalegSortedByNomorUrut() {
        List<Caleg> calegs = calegService.getAllCaleg("nomor_urut");
        assertEquals(2, calegs.size());
        assertEquals(1, (int) calegs.get(0).getNomorUrut());
        assertEquals(2, (int) calegs.get(1).getNomorUrut());
    }

    @Test
    public void testGetCalegsByDapil() {
        List<Caleg> calegs = calegService.getCalegsByDapil("Dapil 1");
        assertEquals(1, calegs.size());
        assertEquals("Caleg 1", calegs.get(0).getNama());
    }

    @Test
    public void testGetFilteredCaleg() {
        Long dapilId = 1L;
        Long partaiId = 1L;
        List<Caleg> calegs = calegService.getFilteredCaleg(dapilId, partaiId, "nomor_urut");
        assertEquals(1, calegs.size());
    }
}
