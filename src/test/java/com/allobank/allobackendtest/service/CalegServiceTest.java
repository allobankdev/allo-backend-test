package com.allobank.allobackendtest.service;

import com.allobank.allobackendtest.dto.CalegDTO;
import com.allobank.allobackendtest.dto.CalegFilterDTO;
import com.allobank.allobackendtest.mapper.CalegMapper;
import com.allobank.allobackendtest.model.Caleg;
import com.allobank.allobackendtest.model.JenisKelamin;
import com.allobank.allobackendtest.repository.CalegRepository;
import com.allobank.allobackendtest.service.impl.CalegServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.Arrays;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CalegServiceTest {

    @Mock
    private CalegRepository calegRepository;

    @Mock
    private CalegMapper calegMapper;

    @InjectMocks
    private CalegServiceImpl calegService;

    private Caleg testCaleg;
    private CalegDTO testCalegDTO;

    @BeforeEach
    void setUp() {
        testCaleg = new Caleg();
        testCaleg.setId(UUID.randomUUID());
        testCaleg.setNama("Test Caleg");
        testCaleg.setNomorUrut(1);
        testCaleg.setJenisKelamin(JenisKelamin.LAKILAKI);

        testCalegDTO = CalegDTO.builder()
                .id(testCaleg.getId())
                .nama(testCaleg.getNama())
                .nomorUrut(testCaleg.getNomorUrut())
                .jenisKelamin(testCaleg.getJenisKelamin())
                .build();
    }

    @Test
    void testFindAllWithFilter() {
        // Given
        CalegFilterDTO filter = CalegFilterDTO.builder().build();
        Pageable pageable = PageRequest.of(0, 10);
        Page<Caleg> calegPage = new PageImpl<>(Arrays.asList(testCaleg));

        when(calegRepository.findAllWithFilter(any(), any(), any())).thenReturn(calegPage);
        when(calegMapper.toDTO(testCaleg)).thenReturn(testCalegDTO);

        // When
        Page<CalegDTO> result = calegService.findAllWithFilter(filter, pageable);

        // Then
        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        assertEquals(testCalegDTO, result.getContent().get(0));
        verify(calegRepository).findAllWithFilter(null, null, pageable);
    }

    @Test
    void testFindById() {
        // Given
        UUID id = testCaleg.getId();
        when(calegRepository.findById(id)).thenReturn(Optional.of(testCaleg));
        when(calegMapper.toDTO(testCaleg)).thenReturn(testCalegDTO);

        // When
        Optional<CalegDTO> result = calegService.findById(id);

        // Then
        assertTrue(result.isPresent());
        assertEquals(testCalegDTO, result.get());
        verify(calegRepository).findById(id);
    }
}