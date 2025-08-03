package com.allobank.allobackendtest.service;

import com.allobank.allobackendtest.dto.Request.CalegRequestDTO;
import com.allobank.allobackendtest.dto.Response.CalegResponseDTO;
import com.allobank.allobackendtest.entity.CalegEntity;
import com.allobank.allobackendtest.entity.DapilEntity;
import com.allobank.allobackendtest.entity.JenisKelaminEnum;
import com.allobank.allobackendtest.entity.PartaiEntity;
import com.allobank.allobackendtest.repository.CalegRepository;
import com.allobank.allobackendtest.repository.DapilRepository;
import com.allobank.allobackendtest.repository.PartaiRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Sort;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CalegServiceTest {

    @Mock
    private CalegRepository calegRepository;

    @Mock
    private DapilRepository dapilRepository;

    @Mock
    private PartaiRepository partaiRepository;

    @InjectMocks
    private CalegService calegService;

    private UUID dapilId;
    private UUID partaiId;
    private DapilEntity dapil;
    private PartaiEntity partai;
    private CalegEntity caleg;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        dapilId = UUID.randomUUID();
        partaiId = UUID.randomUUID();

        dapil = new DapilEntity();
        dapil.setId(dapilId);
        dapil.setNamaDapil("Jakarta I");

        partai = new PartaiEntity();
        partai.setId(partaiId);
        partai.setNamaPartai("Partai A");

        caleg = new CalegEntity();
        caleg.setId(UUID.randomUUID());
        caleg.setNama("Agus");
        caleg.setNomorUrut(1);
        caleg.setJenisKelamin(JenisKelaminEnum.LAKILAKI);
        caleg.setDapil(dapil);
        caleg.setPartai(partai);
    }

    @Test
    void testGetAllCalegByDapilAndPartai() {
        List<CalegEntity> calegList = List.of(caleg);
        when(calegRepository.findByDapilIdAndPartaiId(eq(dapilId), eq(partaiId), any(Sort.class))).thenReturn(calegList);

        List<CalegResponseDTO> result = calegService.getAllCaleg(dapilId, partaiId, "nomorUrut");

        assertEquals(1, result.size());
        assertEquals("Agus", result.get(0).getNama());
        System.out.println("✅ testGetAllCalegByDapilAndPartai: passed");
    }

    @Test
    void testCreateCalegSuccess() {
        CalegRequestDTO request = new CalegRequestDTO();
        request.setNama("Agus");
        request.setNomorUrut(1);
        request.setJenisKelamin(JenisKelaminEnum.LAKILAKI);
        request.setDapilId(dapilId);
        request.setPartaiId(partaiId);

        when(dapilRepository.findById(dapilId)).thenReturn(Optional.of(dapil));
        when(partaiRepository.findById(partaiId)).thenReturn(Optional.of(partai));
        when(calegRepository.save(any(CalegEntity.class))).thenReturn(caleg);

        CalegResponseDTO response = calegService.create(request);

        assertNotNull(response);
        assertEquals("Agus", response.getNama());
        System.out.println("✅ testCreateCalegSuccess: passed");
    }

    @Test
    void testCreateCalegDapilNotFound() {
        CalegRequestDTO request = new CalegRequestDTO();
        request.setDapilId(dapilId);
        request.setPartaiId(partaiId);

        when(dapilRepository.findById(dapilId)).thenReturn(Optional.empty());

        NoSuchElementException ex = assertThrows(NoSuchElementException.class, () -> calegService.create(request));
        assertEquals("Dapil not found", ex.getMessage());
        System.out.println("✅ testCreateCalegDapilNotFound: passed (exception caught)");
    }

    @Test
    void testCreateCalegPartaiNotFound() {
        CalegRequestDTO request = new CalegRequestDTO();
        request.setDapilId(dapilId);
        request.setPartaiId(partaiId);

        when(dapilRepository.findById(dapilId)).thenReturn(Optional.of(dapil));
        when(partaiRepository.findById(partaiId)).thenReturn(Optional.empty());

        NoSuchElementException ex = assertThrows(NoSuchElementException.class, () -> calegService.create(request));
        assertEquals("Partai not found", ex.getMessage());
        System.out.println("✅ testCreateCalegPartaiNotFound: passed (exception caught)");
    }

    @Test
    void testDeleteCalegNotFound() {
        UUID id = UUID.randomUUID();
        when(calegRepository.existsById(id)).thenReturn(false);

        NoSuchElementException ex = assertThrows(NoSuchElementException.class, () -> calegService.delete(id));
        assertEquals("Caleg not found", ex.getMessage());
        System.out.println("✅ testDeleteCalegNotFound: passed (exception caught)");
    }

    @Test
    void testDeleteCalegSuccess() {
        UUID id = UUID.randomUUID();
        when(calegRepository.existsById(id)).thenReturn(true);
        doNothing().when(calegRepository).deleteById(id);

        assertDoesNotThrow(() -> calegService.delete(id));
        verify(calegRepository, times(1)).deleteById(id);
        System.out.println("✅ testDeleteCalegSuccess: passed");
    }
}
