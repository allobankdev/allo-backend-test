package com.allobank.allobackendtest;

import com.allobank.allobackendtest.controller.CalegController;
import com.allobank.allobackendtest.model.Caleg;
import com.allobank.allobackendtest.service.CalegService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.*;

public class CalegControllerTest {

    private MockMvc mockMvc;

    @Mock
    private CalegService calegService;

    @InjectMocks
    private CalegController calegController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(calegController).build();
    }

    @Test
    public void testGetAllCaleg() throws Exception {
        List<Caleg> calegList = Arrays.asList(new Caleg(), new Caleg());

        when(calegService.getAllCaleg()).thenReturn(calegList);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/caleg/listall")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.size()").value(calegList.size()));

        verify(calegService, times(1)).getAllCaleg();
        verifyNoMoreInteractions(calegService);
    }

    @Test
    public void testGetCalegList() throws Exception {
        List<Caleg> calegList = Arrays.asList(new Caleg(), new Caleg());
        String dapilId = "1";
        String partaiId = "2";

        when(calegService.getCalegList(dapilId, partaiId, "nomorUrut", "ASC")).thenReturn(calegList);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/caleg/list")
                        .param("dapilId", dapilId)
                        .param("partaiId", partaiId)
                        .param("sortBy", "nomorUrut")
                        .param("sortOrder", "ASC")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.size()").value(calegList.size()));

        verify(calegService, times(1)).getCalegList(dapilId, partaiId, "nomorUrut", "ASC");
        verifyNoMoreInteractions(calegService);
    }

    @Test
    public void testGetCalegById() throws Exception {
        String calegId = "1";
        Caleg caleg = new Caleg();
        caleg.setId(calegId);

        when(calegService.getCalegById(calegId)).thenReturn(caleg);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/caleg/{id}", calegId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(calegId));

        verify(calegService, times(1)).getCalegById(calegId);
        verifyNoMoreInteractions(calegService);
    }



    @Test
    public void testDeleteCaleg() throws Exception {
        String calegId = "1";

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/caleg/{id}", calegId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk());

        verify(calegService, times(1)).deleteCaleg(calegId);
        verifyNoMoreInteractions(calegService);
    }

    // Helper method to convert object to JSON string
    private String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }}
