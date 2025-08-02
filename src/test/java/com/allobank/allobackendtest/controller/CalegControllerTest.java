package com.allobank.allobackendtest.controller;

import com.allobank.allobackendtest.dto.CalegDTO;
import com.allobank.allobackendtest.service.CalegService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CalegController.class)
class CalegControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CalegService calegService;

    @Test
    void testGetCalegList() throws Exception {
        // Given
        CalegDTO caleg1 = CalegDTO.builder()
                .id(UUID.randomUUID())
                .nama("Test Caleg 1")
                .nomorUrut(1)
                .build();

        CalegDTO caleg2 = CalegDTO.builder()
                .id(UUID.randomUUID())
                .nama("Test Caleg 2")
                .nomorUrut(2)
                .build();

        Page<CalegDTO> page = new PageImpl<>(
                Arrays.asList(caleg1, caleg2),
                PageRequest.of(0, 10),
                2
        );

        when(calegService.findAllWithFilter(any(), any())).thenReturn(page);

        // When & Then
        mockMvc.perform(get("/caleg"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content.length()").value(2))
                .andExpect(jsonPath("$.content[0].nama").value("Test Caleg 1"))
                .andExpect(jsonPath("$.content[1].nama").value("Test Caleg 2"));
    }

    @Test
    void testGetCalegListWithFilters() throws Exception {
        // Given
        UUID dapilId = UUID.randomUUID();
        UUID partaiId = UUID.randomUUID();

        Page<CalegDTO> page = new PageImpl<>(Arrays.asList(), PageRequest.of(0, 10), 0);
        when(calegService.findAllWithFilter(any(), any())).thenReturn(page);

        // When & Then
        mockMvc.perform(get("/caleg")
                        .param("dapilId", dapilId.toString())
                        .param("partaiId", partaiId.toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray());
    }
}