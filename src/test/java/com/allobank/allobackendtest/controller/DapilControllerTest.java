package com.allobank.allobackendtest.controller;

import com.allobank.allobackendtest.model.Dapil;
import com.allobank.allobackendtest.service.DapilService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class DapilControllerTest {

    @InjectMocks
    private DapilController dapilController;

    @Mock
    private DapilService dapilService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllDapils_Success() {
        // Mocking service response
        List<Dapil> dapilList = Arrays.asList(
                new Dapil(1L, "Dapil A", "Provinsi A", Arrays.asList("Wilayah 1", "Wilayah 2"), 5),
                new Dapil(2L, "Dapil B", "Provinsi B", Arrays.asList("Wilayah 3", "Wilayah 4"), 3)
        );
        when(dapilService.getAllDapils()).thenReturn(dapilList);

        // Call the controller method
        ResponseEntity<List<Dapil>> response = dapilController.getAllDapils();

        // Verify the result
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(2, response.getBody().size());
        assertEquals("Dapil A", response.getBody().get(0).getNamaDapil());
        verify(dapilService, times(1)).getAllDapils();
    }

    @Test
    void testAddDapil_Success() {
        // Mocking request
        Dapil dapil = new Dapil(null, "Dapil A", "Provinsi A", Arrays.asList("Wilayah 1", "Wilayah 2"), 5);
        when(dapilService.addDapil(dapil)).thenReturn(new Dapil(1L, "Dapil A", "Provinsi A", Arrays.asList("Wilayah 1", "Wilayah 2"), 5));

        // Call the controller method
        ResponseEntity<String> response = dapilController.addDapil(dapil);

        // Verify the result
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals("Dapil created successfully: Dapil A", response.getBody());
        verify(dapilService, times(1)).addDapil(dapil);
    }

    @Test
    void testAddDapil_BadRequest() {
        // Mocking request with an exception
        Dapil dapil = new Dapil(null, "Dapil A", "Provinsi A", Arrays.asList("Wilayah 1", "Wilayah 2"), 5);
        when(dapilService.addDapil(dapil)).thenThrow(new IllegalArgumentException("Dapil name cannot be null or empty"));

        // Call the controller method
        ResponseEntity<String> response = dapilController.addDapil(dapil);

        // Verify the result
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Dapil name cannot be null or empty", response.getBody());
        verify(dapilService, times(1)).addDapil(dapil);
    }

    @Test
    void testGetDapilById_Success() {
        // Mocking service response
        Dapil dapil = new Dapil(1L, "Dapil A", "Provinsi A", Arrays.asList("Wilayah 1", "Wilayah 2"), 5);
        when(dapilService.getDapilById(1L)).thenReturn(Optional.of(dapil));

        // Call the controller method
        ResponseEntity<Dapil> response = dapilController.getDapilById(1L);

        // Verify the result
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Dapil A", response.getBody().getNamaDapil());
        verify(dapilService, times(1)).getDapilById(1L);
    }

    @Test
    void testGetDapilById_NotFound() {
        // Mocking service response for not found
        when(dapilService.getDapilById(1L)).thenReturn(Optional.empty());

        // Call the controller method
        ResponseEntity<Dapil> response = dapilController.getDapilById(1L);

        // Verify the result
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        verify(dapilService, times(1)).getDapilById(1L);
    }
}
