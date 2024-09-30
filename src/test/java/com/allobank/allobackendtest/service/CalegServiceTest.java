package com.allobank.allobackendtest.service;

import com.allobank.allobackendtest.dto.CalegRequest;
import com.allobank.allobackendtest.exception.EntityNotFoundException;
import com.allobank.allobackendtest.model.Caleg;
import com.allobank.allobackendtest.model.Dapil;
import com.allobank.allobackendtest.model.JenisKelamin;
import com.allobank.allobackendtest.model.Partai;
import com.allobank.allobackendtest.repository.CalegRepository;
import com.allobank.allobackendtest.repository.DapilRepository;
import com.allobank.allobackendtest.repository.PartaiRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class CalegServiceTest {

    @InjectMocks
    private CalegService calegService;

    @Mock
    private CalegRepository calegRepository;

    @Mock
    private PartaiRepository partaiRepository;

    @Mock
    private DapilRepository dapilRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllCalegs_withSorting() {
        // Given
        String namaDapil = "Dapil 1";
        String namaPartai = "Partai 1";
        String sortDirection = "ASC";
        Pageable pageable = PageRequest.of(0, 10, Sort.by("nomor_urut").ascending());

        // Create test data
        Caleg caleg1 = new Caleg();
        caleg1.setNama("Caleg 1");
        caleg1.setNomorUrut(1);

        Caleg caleg2 = new Caleg();
        caleg2.setNama("Caleg 2");
        caleg2.setNomorUrut(2);

        List<Caleg> calegList = Arrays.asList(caleg1, caleg2);
        Page<Caleg> page = new PageImpl<>(calegList, pageable, calegList.size());

        // Mock the repository behavior
        when(calegRepository.findCalegWithSorting(namaDapil, namaPartai, pageable)).thenReturn(page);

        // When
        Page<Caleg> result = calegService.getAllCalegs(namaDapil, namaPartai, sortDirection, pageable);

        // Then
        assertNotNull(result);
        assertEquals(2, result.getTotalElements());
        assertEquals("Caleg 1", result.getContent().get(0).getNama());
        assertEquals("Caleg 2", result.getContent().get(1).getNama());
        verify(calegRepository, times(1)).findCalegWithSorting(namaDapil, namaPartai, pageable);
    }



    @Test
    void testGetAllCalegs_withoutSorting() {
        String namaDapil = "Dapil 1";
        String namaPartai = "Partai 1";
        Pageable pageable = Pageable.ofSize(10);

        Caleg caleg = new Caleg();
        caleg.setNama("Caleg 1");

        when(calegRepository.findCalegByDapilNameAndPartaiNameWithoutSort(namaDapil, namaPartai))
                .thenReturn(Collections.singletonList(caleg));

        Page<Caleg> result = calegService.getAllCalegs(namaDapil, namaPartai, null, pageable);

        assertEquals(1, result.getTotalElements());
        assertEquals("Caleg 1", result.getContent().get(0).getNama());
        verify(calegRepository, times(1)).findCalegByDapilNameAndPartaiNameWithoutSort(namaDapil, namaPartai);
    }

    @Test
    void testAddCaleg_success() {
        CalegRequest request = new CalegRequest();
        request.setDapilId(1L);
        request.setPartaiId(1L);
        request.setNama("Caleg 1");
        request.setNomorUrut(1);
        request.setJenisKelamin(JenisKelamin.LAKILAKI);

        Dapil dapil = new Dapil();
        Partai partai = new Partai();

        when(dapilRepository.findById(1L)).thenReturn(Optional.of(dapil));
        when(partaiRepository.findById(1L)).thenReturn(Optional.of(partai));
        when(calegRepository.existsByNomorUrut(1)).thenReturn(false);
        when(calegRepository.save(any(Caleg.class))).thenReturn(new Caleg());

        Caleg result = calegService.addCaleg(request);

        assertNotNull(result);
        verify(dapilRepository, times(1)).findById(1L);
        verify(partaiRepository, times(1)).findById(1L);
        verify(calegRepository, times(1)).existsByNomorUrut(1);
        verify(calegRepository, times(1)).save(any(Caleg.class));
    }

    @Test
    void testAddCaleg_dapilNotFound() {
        CalegRequest request = new CalegRequest();
        request.setDapilId(1L);
        request.setPartaiId(1L);
        request.setNama("Caleg 1");
        request.setNomorUrut(1);
        request.setJenisKelamin(JenisKelamin.LAKILAKI);

        when(dapilRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> calegService.addCaleg(request));
        verify(dapilRepository, times(1)).findById(1L);
    }

    @Test
    void testAddCaleg_partaiNotFound() {
        CalegRequest request = new CalegRequest();
        request.setDapilId(1L);
        request.setPartaiId(1L);
        request.setNama("Caleg 1");
        request.setNomorUrut(1);
        request.setJenisKelamin(JenisKelamin.LAKILAKI);

        Dapil dapil = new Dapil();

        when(dapilRepository.findById(1L)).thenReturn(Optional.of(dapil));
        when(partaiRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> calegService.addCaleg(request));
        verify(partaiRepository, times(1)).findById(1L);
    }

    @Test
    void testAddCaleg_invalidNomorUrut() {
        CalegRequest request = new CalegRequest();
        request.setDapilId(1L);
        request.setPartaiId(1L);
        request.setNama("Caleg 1");
        request.setNomorUrut(1);
        request.setJenisKelamin(JenisKelamin.LAKILAKI);

        Dapil dapil = new Dapil();
        Partai partai = new Partai();

        when(dapilRepository.findById(1L)).thenReturn(Optional.of(dapil));
        when(partaiRepository.findById(1L)).thenReturn(Optional.of(partai));
        when(calegRepository.existsByNomorUrut(1)).thenReturn(true);

        assertThrows(IllegalArgumentException.class, () -> calegService.addCaleg(request));
        verify(calegRepository, times(1)).existsByNomorUrut(1);
    }
}
