package com.allobank.allobackendtest.controller;

import com.allobank.allobackendtest.exception.PartaiNotFoundException;
import com.allobank.allobackendtest.model.Partai;
import com.allobank.allobackendtest.service.PartaiService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

class PartaiControllerTest {

    @InjectMocks
    private PartaiController partaiController;

    @Mock
    private PartaiService partaiService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllPartais_Success() {
        // Mocking service response
        List<Partai> partaiList = Arrays.asList(
                new Partai(1L, "Partai A",1),
                new Partai(2L, "Partai B",2)
        );
        when(partaiService.getAllParties()).thenReturn(partaiList);

        // Call the controller method
        ResponseEntity<List<Partai>> response = partaiController.getAllPartais();

        // Verify the response
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(partaiList, response.getBody());
    }

    @Test
    void testAddPartai_Success() {
        // Mocking request
        Partai partai = new Partai(1L, "Partai A",1);

        // Mocking service response
        when(partaiService.addPartai(partai)).thenReturn(partai);

        // Call the controller method
        ResponseEntity<?> response = partaiController.addPartai(partai);

        // Verify the response
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(partai, response.getBody());
    }

    @Test
    void testAddPartai_IllegalArgumentException() {
        // Mocking request
        Partai partai = new Partai();

        // Mocking exception
        when(partaiService.addPartai(partai)).thenThrow(new IllegalArgumentException("Partai name cannot be null or empty"));

        // Call the controller method
        ResponseEntity<?> response = partaiController.addPartai(partai);

        // Verify the response
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Partai name cannot be null or empty", response.getBody());
    }

    @Test
    void testGetPartaiById_Success() {
        // Mocking response
        Partai partai = new Partai(1L, "Partai A",1);

        // Mocking service
        when(partaiService.getPartaiById(1L)).thenReturn(partai);

        // Call the controller method
        ResponseEntity<?> response = partaiController.getPartaiById(1L);

        // Verify the response
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(partai, response.getBody());
    }

    @Test
    void testGetPartaiById_NotFound() {
        // Mocking exception
        when(partaiService.getPartaiById(1L)).thenThrow(new PartaiNotFoundException(1L));

        // Call the controller method
        ResponseEntity<?> response = partaiController.getPartaiById(1L);

        // Verify the response
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Partai with ID 1 is not registered, please register it first.", response.getBody());
    }
}
