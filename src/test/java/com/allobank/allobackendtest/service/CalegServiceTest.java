package com.allobank.allobackendtest.service;

import com.allobank.allobackendtest.common.exception.EntityNotFoundException;
import com.allobank.allobackendtest.dto.CalegDto;
import com.allobank.allobackendtest.entity.CalegEntity;
import com.allobank.allobackendtest.entity.DapilEntity;
import com.allobank.allobackendtest.entity.PartaiEntity;
import com.allobank.allobackendtest.mapper.CalegMapper;
import com.allobank.allobackendtest.model.Caleg;
import com.allobank.allobackendtest.model.JenisKelamin;
import com.allobank.allobackendtest.repository.CalegRepository;
import com.allobank.allobackendtest.repository.DapilRepository;
import com.allobank.allobackendtest.repository.PartaiRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CalegServiceTest {

    @Mock
    private CalegRepository calegRepository;

    @Mock
    private DapilRepository dapilRepository;

    @Mock
    private PartaiRepository partaiRepository;

    @Mock
    private CalegMapper calegMapper;

    @InjectMocks
    private CalegService calegService;

    private UUID dapilId;
    private UUID partaiId;
    private CalegDto calegDto;
    private CalegEntity calegEntity;
    private Caleg calegResponse; // pakai mock nanti

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        dapilId = UUID.randomUUID();
        partaiId = UUID.randomUUID();

        calegDto = CalegDto.builder()
                .dapil(dapilId)
                .partai(partaiId)
                .nomorUrut(1)
                .nama("John Doe")
                .jenisKelamin(JenisKelamin.LAKILAKI)
                .build();


        calegEntity = new CalegEntity();
        calegEntity.setId(UUID.randomUUID());
        calegEntity.setNomorUrut(1);
        calegEntity.setNama("John Doe");

        DapilEntity dapilEntity = new DapilEntity();
        dapilEntity.setId(dapilId);
        dapilEntity.setNamaDapil("Dapil 1");

        PartaiEntity partaiEntity = new PartaiEntity();
        partaiEntity.setId(partaiId);
        partaiEntity.setNamaPartai("Partai A");

        calegEntity.setDapil(dapilEntity);
        calegEntity.setPartai(partaiEntity);

        // pakai mock supaya gak perlu akses constructor Caleg
        calegResponse = mock(Caleg.class);
        when(calegResponse.getId()).thenReturn(calegEntity.getId());
        when(calegResponse.getNama()).thenReturn("John Doe");
        when(calegResponse.getNomorUrut()).thenReturn(1);
    }

    @Test
    void testCreateCaleg_Success() {
        when(calegMapper.toEntity(calegDto)).thenReturn(calegEntity);
        when(dapilRepository.findById(dapilId)).thenReturn(Optional.of(calegEntity.getDapil()));
        when(partaiRepository.findById(partaiId)).thenReturn(Optional.of(calegEntity.getPartai()));
        when(calegRepository.existsByDapilIdAndNomorUrut(dapilId, 1)).thenReturn(false);
        when(calegRepository.save(calegEntity)).thenReturn(calegEntity);
        when(calegMapper.toResponse(calegEntity)).thenReturn(calegResponse);

        Caleg result = calegService.create(calegDto);

        assertNotNull(result);
        assertEquals("John Doe", result.getNama());
        verify(calegRepository, times(1)).save(calegEntity);
    }

    @Test
    void testCreateCaleg_DapilNotFound() {
        when(calegMapper.toEntity(calegDto)).thenReturn(calegEntity);
        when(dapilRepository.findById(dapilId)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> calegService.create(calegDto));
    }

    @Test
    void testCreateCaleg_PartaiNotFound() {
        when(calegMapper.toEntity(calegDto)).thenReturn(calegEntity);
        when(dapilRepository.findById(dapilId)).thenReturn(Optional.of(calegEntity.getDapil()));
        when(partaiRepository.findById(partaiId)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> calegService.create(calegDto));
    }

    @Test
    void testCreateCaleg_DuplicateNomorUrut() {
        when(calegMapper.toEntity(calegDto)).thenReturn(calegEntity);
        when(dapilRepository.findById(dapilId)).thenReturn(Optional.of(calegEntity.getDapil()));
        when(partaiRepository.findById(partaiId)).thenReturn(Optional.of(calegEntity.getPartai()));
        when(calegRepository.existsByDapilIdAndNomorUrut(dapilId, 1)).thenReturn(true);

        assertThrows(IllegalArgumentException.class, () -> calegService.create(calegDto));
    }
}
