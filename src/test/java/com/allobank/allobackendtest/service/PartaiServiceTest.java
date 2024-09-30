package com.allobank.allobackendtest.service;

import com.allobank.allobackendtest.exception.PartaiNotFoundException;
import com.allobank.allobackendtest.model.Partai;
import com.allobank.allobackendtest.repository.PartaiRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PartaiServiceTest {

    @InjectMocks
    private PartaiService partaiService;

    @Mock
    private PartaiRepository partaiRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllParties_Success() {
        // Mocking repository response
        List<Partai> partaiList = Arrays.asList(
                new Partai(1L, "Partai A", 1),
                new Partai(2L, "Partai B", 2)
        );
        when(partaiRepository.findAll()).thenReturn(partaiList);

        // Call the service method
        List<Partai> result = partaiService.getAllParties();

        // Verify the result
        assertEquals(2, result.size());
        assertEquals("Partai A", result.get(0).getNamaPartai());
        assertEquals(1, result.get(0).getNomorUrut());
    }

    @Test
    void testAddPartai_Success() {
        // Mocking request and repository response
        Partai partai = new Partai(1L, "Partai A", 1);
        when(partaiRepository.findByNamaPartaiIgnoreCase(partai.getNamaPartai())).thenReturn(List.of());
        when(partaiRepository.save(partai)).thenReturn(partai);

        // Call the service method
        Partai result = partaiService.addPartai(partai);

        // Verify the result
        assertNotNull(result);
        assertEquals("Partai A", result.getNamaPartai());
        assertEquals(1, result.getNomorUrut());
        verify(partaiRepository, times(1)).save(partai);
    }

    @Test
    void testAddPartai_IllegalArgumentException_NameEmpty() {
        // Mocking request with empty name
        Partai partai = new Partai(1L, "", 1);

        // Call the service method and verify the exception
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            partaiService.addPartai(partai);
        });
        assertEquals("Partai name cannot be null or empty", exception.getMessage());
    }

    @Test
    void testAddPartai_IllegalArgumentException_NameAlreadyExists() {
        // Mocking request and repository response
        Partai partai = new Partai(1L, "Partai A", 1);
        when(partaiRepository.findByNamaPartaiIgnoreCase(partai.getNamaPartai()))
                .thenReturn(List.of(partai));

        // Call the service method and verify the exception
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            partaiService.addPartai(partai);
        });
        assertEquals("Partai already registered, try with another one", exception.getMessage());
    }

    @Test
    void testGetPartaiById_Success() {
        // Mocking repository response
        Partai partai = new Partai(1L, "Partai A", 1);
        when(partaiRepository.findById(1L)).thenReturn(Optional.of(partai));

        // Call the service method
        Partai result = partaiService.getPartaiById(1L);

        // Verify the result
        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("Partai A", result.getNamaPartai());
    }

    @Test
    void testGetPartaiById_NotFound() {
        // Mocking repository response for not found
        when(partaiRepository.findById(1L)).thenReturn(Optional.empty());

        // Call the service method and verify the exception
        PartaiNotFoundException exception = assertThrows(PartaiNotFoundException.class, () -> {
            partaiService.getPartaiById(1L);
        });
        assertEquals("Partai not found: 1", exception.getMessage());
    }
}
