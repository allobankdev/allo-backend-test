package com.allobank.allobackendtest.controller;

import com.allobank.allobackendtest.model.Caleg;
import com.allobank.allobackendtest.service.CalegService;
import com.allobank.allobackendtest.exception.CalegNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class CalegControllerTests {

    private MockMvc mockMvc;

    @Mock
    private CalegService calegService;

    @InjectMocks
    private CalegController calegController;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(calegController).build();
    }

    @Test
    public void testGetCalegs_Success() throws Exception {
        Caleg caleg1 = new Caleg();
        Caleg caleg2 = new Caleg();
        List<Caleg> calegs = Arrays.asList(caleg1, caleg2);

        when(calegService.getAllCalegs(any(String.class), any(String.class), any(Integer.class), any(Integer.class), any(String.class)))
                .thenReturn(calegs);

        mockMvc.perform(get("/api/calegs/all")
                        .param("namaDapil", "Dapil1")
                        .param("namaPartai", "PartaiA")
                        .param("page", "0")
                        .param("size", "10")
                        .param("sortBy", "nomorUrut")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0]").exists());
    }

    @Test
    public void testGetCalegs_NotFound() throws Exception {
        when(calegService.getAllCalegs(any(String.class), any(String.class), any(Integer.class), any(Integer.class), any(String.class)))
                .thenThrow(new CalegNotFoundException("Caleg tidak ditemukan"));

        mockMvc.perform(get("/api/calegs/all")
                        .param("namaDapil", "DapilSalah")
                        .param("namaPartai", "PartaiA")
                        .param("page", "0")
                        .param("size", "10")
                        .param("sortBy", "nomorUrut")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message").value("Caleg tidak ditemukan"));
    }
}
