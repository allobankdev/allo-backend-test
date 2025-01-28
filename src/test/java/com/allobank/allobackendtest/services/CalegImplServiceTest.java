package com.allobank.allobackendtest.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

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
import org.springframework.data.domain.Sort;

import com.allobank.allobackendtest.repositories.CalegRepository;
import com.allobank.allobackendtest.responses.ResponseMsg;

@ExtendWith(MockitoExtension.class)
public class CalegImplServiceTest {

    @Mock
    private CalegRepository calegRepository;

    @InjectMocks
    private CalegImplService calegImplService;

    private Page<Map<String, Object>> mockPage;

    @BeforeEach
    void setUp() {
        // Initialize a mock page with some dummy data
        Map<String, Object> caleg1 = new HashMap<>();
        caleg1.put("id", UUID.randomUUID());
        caleg1.put("partai_id", UUID.randomUUID());
        caleg1.put("dapil_id", UUID.randomUUID());
        caleg1.put("nama", "Caleg A");
        caleg1.put("nama_dapil", "Dapil 1");
        caleg1.put("nama_partai", "Partai A");
        caleg1.put("jenis_kelamin", "LAKILAKI");
        caleg1.put("nomor_urut", 1);

        Map<String, Object> caleg2 = new HashMap<>();
        caleg2.put("id", UUID.randomUUID());
        caleg1.put("partai_id", UUID.randomUUID());
        caleg1.put("dapil_id", UUID.randomUUID());
        caleg2.put("nama", "Caleg B");
        caleg2.put("nama_dapil", "Dapil 2");
        caleg2.put("nama_partai", "Partai B");
        caleg1.put("jenis_kelamin", "LAKILAKI");
        caleg1.put("nomor_urut", 1);

        mockPage = new PageImpl<>(Arrays.asList(caleg1, caleg2), PageRequest.of(0, 10), 2);
    }

    @Test
    void testSearchCalegSuccess() {
        // Arrange
        when(calegRepository.searchCaleg(any(Pageable.class), any(String.class), any(String.class)))
                .thenReturn(mockPage);

        // Act
        ResponseMsg<HashMap<String, Object>> response = calegImplService.SearchCaleg(0, 10, "nomorurut", "asc",
                "Partai A", "Dapil 1");

        // Assert
        assertEquals("00", response.getRc());
        assertEquals("Data Caleg Berhasil Ditampilkan", response.getRm());
        assertEquals(2L, response.getData().get("totalItems"));
        assertEquals(0, response.getData().get("currentPage"));
        assertEquals(1, response.getData().get("totalPages"));
        assertEquals(2, ((java.util.List<?>) response.getData().get("data")).size());
    }

    @Test
    void testSearchCalegException() {
        // Arrange
        when(calegRepository.searchCaleg(any(Pageable.class), any(String.class), any(String.class)))
                .thenThrow(new RuntimeException("Database error"));

        // Act
        ResponseMsg<HashMap<String, Object>> response = calegImplService.SearchCaleg(0, 10, "nomorurut", "asc",
                "Partai A", "Dapil 1");

        // Assert
        assertEquals("99", response.getRc());
        assertEquals("Database error", response.getRm());
    }

    @Test
    void testSearchCalegNoSorting() {
        // Arrange
        when(calegRepository.searchCaleg(any(Pageable.class), any(String.class), any(String.class)))
                .thenReturn(mockPage);

        // Act
        ResponseMsg<HashMap<String, Object>> response = calegImplService.SearchCaleg(0, 10, null, null, "Partai A",
                "Dapil 1");

        // Assert
        assertEquals("00", response.getRc());
        assertEquals("Data Caleg Berhasil Ditampilkan", response.getRm());
        assertEquals(2L, response.getData().get("totalItems"));
        assertEquals(0, response.getData().get("currentPage"));
        assertEquals(1, response.getData().get("totalPages"));
        assertEquals(2, ((java.util.List<?>) response.getData().get("data")).size());
    }

    @Test
    void testSearchCalegNoFilters() {
        // Arrange
        Pageable expectedPageable = PageRequest.of(0, 10, Sort.by("nomor_urut").ascending());
        when(calegRepository.searchCaleg(expectedPageable, null, null))
                .thenReturn(mockPage);

        // Act
        ResponseMsg<HashMap<String, Object>> response = calegImplService.SearchCaleg(0, 10, "nomor_urut", "asc", null,
                null);

        // Assert
        assertEquals("00", response.getRc());
        assertEquals("Data Caleg Berhasil Ditampilkan", response.getRm());
        assertEquals(2L, response.getData().get("totalItems"));
        assertEquals(0, response.getData().get("currentPage"));
        assertEquals(1, response.getData().get("totalPages"));
        assertEquals(2, ((java.util.List<?>) response.getData().get("data")).size());
    }
}