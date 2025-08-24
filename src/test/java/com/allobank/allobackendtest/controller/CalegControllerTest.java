package com.allobank.allobackendtest.controller;

import com.allobank.allobackendtest.model.Caleg;
import com.allobank.allobackendtest.model.JenisKelamin;
import com.allobank.allobackendtest.service.CalegService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CalegController.class)
class CalegControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CalegService calegService;

    @Autowired
    private ObjectMapper objectMapper;

    private UUID calegId;
    private Caleg calegResponse;

    @BeforeEach
    void setUp() {
        calegId = UUID.randomUUID();

        // Mock entity response
        calegResponse = Mockito.mock(Caleg.class);
        when(calegResponse.getId()).thenReturn(calegId);
        when(calegResponse.getNama()).thenReturn("John Doe");
        when(calegResponse.getNomorUrut()).thenReturn(1);
        when(calegResponse.getJenisKelamin()).thenReturn(JenisKelamin.LAKILAKI);
    }

    @Test
    void testGetAllCaleg() throws Exception {
        Pageable pageable = PageRequest.of(0, 10);
        when(calegService.getAllCaleg(any(Pageable.class), any(), any(), any(), any()))
                .thenReturn(new PageImpl<>(List.of(calegResponse)));

        mockMvc.perform(get("/caleg")
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data[0].nama").value("John Doe"));
    }

    @Test
    void testGetById() throws Exception {
        when(calegService.getById(calegId)).thenReturn(calegResponse);

        mockMvc.perform(get("/caleg/{id}", calegId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.nama").value("John Doe"));
    }

    @Test
    void testCreate() throws Exception {
        when(calegService.create(any())).thenReturn(calegResponse);

        String calegJson = """
            {
              "dapil": "11111111-1111-1111-1111-111111111111",
              "partai": "22222222-2222-2222-2222-222222222222",
              "nomorUrut": 1,
              "nama": "John Doe",
              "jenisKelamin": "LAKILAKI"
            }
            """;


        mockMvc.perform(post("/caleg")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(calegJson))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.data.nama").value("John Doe"));
    }

    @Test
    void testUpdate() throws Exception {
        when(calegService.update(eq(calegId), any())).thenReturn(calegResponse);

        String calegJson = """
                {
                  "dapil": "11111111-1111-1111-1111-111111111111",
                  "partai": "22222222-2222-2222-2222-222222222222",
                  "nomorUrut": 1,
                  "nama": "John Doe",
                  "jenisKelamin": "LAKILAKI"
                }
                """;

        mockMvc.perform(put("/caleg/{id}", calegId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(calegJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.nama").value("John Doe"));
    }

    @Test
    void testDelete() throws Exception {
        doNothing().when(calegService).delete(calegId);

        mockMvc.perform(delete("/caleg/{id}", calegId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Successfully Delete"));

        verify(calegService, times(1)).delete(calegId);
    }

    @Test
    void testGetByNama() throws Exception {
        when(calegService.getByNama("John Doe")).thenReturn(List.of(calegResponse));

        mockMvc.perform(get("/caleg")
                        .param("nama", "John Doe"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data[0].nama").value("John Doe"));
    }
}
