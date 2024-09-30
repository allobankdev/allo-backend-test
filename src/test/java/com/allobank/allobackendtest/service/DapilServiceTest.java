package com.allobank.allobackendtest.service;

import com.allobank.allobackendtest.model.Dapil;
import com.allobank.allobackendtest.repository.DapilRepository;
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

class DapilServiceTest {

    @InjectMocks
    private DapilService dapilService;

    @Mock
    private DapilRepository dapilRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllDapils_Success() {
        // Mocking repository response
        List<Dapil> dapilList = Arrays.asList(
                new Dapil(1L, "Dapil A", "Provinsi A", Arrays.asList("Wilayah 1", "Wilayah 2"), 5),
                new Dapil(2L, "Dapil B", "Provinsi B", Arrays.asList("Wilayah 3", "Wilayah 4"), 3)
        );
        when(dapilRepository.findAll()).thenReturn(dapilList);

        // Call the service method
        List<Dapil> result = dapilService.getAllDapils();

        // Verify the result
        assertEquals(2, result.size());
        assertEquals("Dapil A", result.get(0).getNamaDapil());
    }

    @Test
    void testAddDapil_Success() {
        // Mocking request and repository response
        Dapil dapil = new Dapil(null, "Dapil A", "Provinsi A", Arrays.asList("Wilayah 1", "Wilayah 2"), 5);
        when(dapilRepository.findByNamaDapilIgnoreCase(dapil.getNamaDapil())).thenReturn(List.of());
        when(dapilRepository.save(dapil)).thenReturn(new Dapil(1L, "Dapil A", "Provinsi A", Arrays.asList("Wilayah 1", "Wilayah 2"), 5));

        // Call the service method
        Dapil result = dapilService.addDapil(dapil);

        // Verify the result
        assertNotNull(result);
        assertEquals("Dapil A", result.getNamaDapil());
        assertEquals("Provinsi A", result.getProvinsi());
        assertEquals(5, result.getJumlahKursi());
        verify(dapilRepository, times(1)).save(dapil);
    }

    @Test
    void testAddDapil_IllegalArgumentException_NameEmpty() {
        // Mocking request with empty name
        Dapil dapil = new Dapil(1L, "", "Provinsi A", Arrays.asList("Wilayah 1", "Wilayah 2"), 5);

        // Call the service method and verify the exception
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            dapilService.addDapil(dapil);
        });
        assertEquals("Dapil name cannot be null or empty", exception.getMessage());
    }

    @Test
    void testAddDapil_IllegalArgumentException_NameAlreadyExists() {
        // Mocking request and repository response
        Dapil dapil = new Dapil(1L, "Dapil A", "Provinsi A", Arrays.asList("Wilayah 1", "Wilayah 2"), 5);
        when(dapilRepository.findByNamaDapilIgnoreCase(dapil.getNamaDapil()))
                .thenReturn(List.of(dapil));

        // Call the service method and verify the exception
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            dapilService.addDapil(dapil);
        });
        assertEquals("Dapil already registered, try with another one", exception.getMessage());
    }

    @Test
    void testGetDapilById_Success() {
        // Mocking repository response
        Dapil dapil = new Dapil(1L, "Dapil A", "Provinsi A", Arrays.asList("Wilayah 1", "Wilayah 2"), 5);
        when(dapilRepository.findById(1L)).thenReturn(Optional.of(dapil));

        // Call the service method
        Optional<Dapil> result = dapilService.getDapilById(1L);

        // Verify the result
        assertTrue(result.isPresent());
        assertEquals("Dapil A", result.get().getNamaDapil());
    }

    @Test
    void testGetDapilById_NotFound() {
        // Mocking repository response for not found
        when(dapilRepository.findById(1L)).thenReturn(Optional.empty());

        // Call the service method
        Optional<Dapil> result = dapilService.getDapilById(1L);

        // Verify that the result is empty
        assertFalse(result.isPresent());
    }
}
