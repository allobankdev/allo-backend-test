package com.allobank.allobackendtest.controller;

import com.allobank.allobackendtest.dto.CalegRequest;
import com.allobank.allobackendtest.exception.EntityNotFoundException;
import com.allobank.allobackendtest.model.Caleg;
import com.allobank.allobackendtest.model.JenisKelamin;
import com.allobank.allobackendtest.service.CalegService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

class CalegControllerTest {

    @InjectMocks
    private CalegController calegController;

    @Mock
    private CalegService calegService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllCalegs() {
        // Given
        String namaDapil = "Dapil 1";
        String namaPartai = "Partai 1";
        String sortDirection = "ASC";
        Pageable pageable = PageRequest.of(0, 10);

        Caleg caleg1 = new Caleg();
        caleg1.setNama("Caleg 1");

        Caleg caleg2 = new Caleg();
        caleg2.setNama("Caleg 2");

        List<Caleg> calegList = Arrays.asList(caleg1, caleg2);
        Page<Caleg> page = new PageImpl<>(calegList, pageable, calegList.size());

        // Mock the service method
        when(calegService.getAllCalegs(namaDapil, namaPartai, sortDirection, pageable)).thenReturn(page);

        // When
        ResponseEntity<?> response = calegController.getAllCalegs(namaDapil, namaPartai, sortDirection, pageable);

        // Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(page, response.getBody());
        verify(calegService, times(1)).getAllCalegs(namaDapil, namaPartai, sortDirection, pageable);
    }

    @Test
    void testAddCaleg() {
        // Given
        CalegRequest request = new CalegRequest();
        request.setDapilId(1L);
        request.setPartaiId(1L);
        request.setNama("Caleg 1");
        request.setNomorUrut(1);
        request.setJenisKelamin(JenisKelamin.LAKILAKI);

        Caleg expectedCaleg = new Caleg();
        expectedCaleg.setNama(request.getNama());
        expectedCaleg.setNomorUrut(request.getNomorUrut());

        // Mock the service method
        when(calegService.addCaleg(request)).thenReturn(expectedCaleg);

        // When
        ResponseEntity<?> response = calegController.addCaleg(request);

        // Then
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(expectedCaleg, response.getBody());
        verify(calegService, times(1)).addCaleg(request);
    }

    @Test
    void testAddCaleg_EntityNotFound() {
        // Given
        CalegRequest request = new CalegRequest();
        request.setDapilId(1L);
        request.setPartaiId(1L);
        request.setNama("Caleg 1");
        request.setNomorUrut(1);
        request.setJenisKelamin(JenisKelamin.LAKILAKI);

        // Mock the service method to throw EntityNotFoundException
        when(calegService.addCaleg(request)).thenThrow(new EntityNotFoundException("Dapil not found"));

        // When
        ResponseEntity<?> response = calegController.addCaleg(request);

        // Then
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertTrue(response.getBody().toString().contains("Not Found"));
    }

    @Test
    void testAddCaleg_UnexpectedError() {
        // Given
        CalegRequest request = new CalegRequest();
        request.setDapilId(1L);
        request.setPartaiId(1L);
        request.setNama("Caleg 1");
        request.setNomorUrut(1);
        request.setJenisKelamin(JenisKelamin.LAKILAKI);

        // Mock the service method to throw a generic exception
        when(calegService.addCaleg(request)).thenThrow(new RuntimeException("Unexpected error"));

        // When
        ResponseEntity<?> response = calegController.addCaleg(request);

        // Then
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertTrue(response.getBody().toString().contains("An unexpected error occurred"));
    }
}
