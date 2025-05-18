package com.allobank.allobackendtest.controller;

import com.allobank.allobackendtest.model.JenisKelamin;
import com.allobank.allobackendtest.service.CalegService;
import com.allobank.allobackendtest.util.RequestBodyCaleg;
import com.allobank.allobackendtest.util.RequestGetCaleg;
import com.allobank.allobackendtest.util.dto.CalegResponseDTO;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CalegController.class)
class CalegControllerTest {

    @MockBean
    private CalegService calegService;

    @Autowired
    private MockMvc mockMvc;

    @Test
    void getAllCaleg() throws Exception {
        RequestGetCaleg requestGetCaleg = new RequestGetCaleg();
        requestGetCaleg.setDapil("Jakarta I");
        requestGetCaleg.setPartai("Partai Sejahtera");

        ObjectMapper objectMapper = new ObjectMapper();
        String requestJson = objectMapper.writeValueAsString(requestGetCaleg);

        mockMvc.perform(post("/api/pemilu/get/caleg").contentType(MediaType.APPLICATION_JSON).content(requestJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").exists())
                .andExpect(jsonPath("$.status").value(200));
    }

    @Test
    void createCaleg() throws Exception {
        RequestBodyCaleg requestBodyCaleg = new RequestBodyCaleg();
        requestBodyCaleg.setDapil("10cd3d83-219f-42cd-846d-8994dc0e0e1c");
        requestBodyCaleg.setPartai("f4181007-3cd9-4958-8887-014a1e84bdb9");
        requestBodyCaleg.setNama("John Doe");
        requestBodyCaleg.setJenisKelamin(String.valueOf(JenisKelamin.LAKILAKI));
        requestBodyCaleg.setNomorUrut(2);

        CalegResponseDTO responseDTO = new CalegResponseDTO();
        responseDTO.setNama("John Doe");
        responseDTO.setNomorUrut(2);
        responseDTO.setNamaDapil("Jakarta I");
        responseDTO.setNamaPartai("Partai Gerindra");
        responseDTO.setJenisKelamin(JenisKelamin.LAKILAKI);

        ObjectMapper objectMapper = new ObjectMapper();
        String requestJson = objectMapper.writeValueAsString(requestBodyCaleg);

        mockMvc.perform(post("/api/pemilu/post/caleg")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestJson)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.data").exists())
                .andExpect(jsonPath("$.data.nama").value("John Doe"))
                .andExpect(jsonPath("$.data.nomorUrut").value(2))
                .andExpect(jsonPath("$.data.jenisKelamin").value("LAKI_LAKI"))
                .andExpect(jsonPath("$.status").value(201));

    }

    @Test
    void updateCaleg() throws Exception {
        // Buat caleg terlebih dahulu
        RequestBodyCaleg requestBodyCaleg = new RequestBodyCaleg();
        requestBodyCaleg.setDapil("10cd3d83-219f-42cd-846d-8994dc0e0e1c");
        requestBodyCaleg.setPartai("f4181007-3cd9-4958-8887-014a1e84bdb9");
        requestBodyCaleg.setNama("John Doe");
        requestBodyCaleg.setJenisKelamin(String.valueOf(JenisKelamin.LAKILAKI));
        requestBodyCaleg.setNomorUrut(2);

        ObjectMapper objectMapper = new ObjectMapper();
        String requestJson = objectMapper.writeValueAsString(requestBodyCaleg);

        MvcResult result = mockMvc.perform(post("/api/pemilu/post/caleg")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isCreated())
                .andReturn();

        // Ambil ID dari response
        String response = result.getResponse().getContentAsString();
        JsonNode jsonNode = objectMapper.readTree(response);
        String calegId = jsonNode.path("data").path("id").asText();

        // Update data
        requestBodyCaleg.setNama("Jane Doe");
        requestBodyCaleg.setNomorUrut(5);

        String updateJson = objectMapper.writeValueAsString(requestBodyCaleg);

        mockMvc.perform(put("/api/pemilu/update/caleg/" + calegId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updateJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.nama").value("Jane Doe"))
                .andExpect(jsonPath("$.data.nomorUrut").value(5));
    }

    @Test
    void deleteCaleg() throws Exception {
        // Buat caleg dulu
        RequestBodyCaleg requestBodyCaleg = new RequestBodyCaleg();
        requestBodyCaleg.setDapil("10cd3d83-219f-42cd-846d-8994dc0e0e1c");
        requestBodyCaleg.setPartai("f4181007-3cd9-4958-8887-014a1e84bdb9");
        requestBodyCaleg.setNama("John Doe");
        requestBodyCaleg.setJenisKelamin(String.valueOf(JenisKelamin.LAKILAKI));
        requestBodyCaleg.setNomorUrut(2);

        ObjectMapper objectMapper = new ObjectMapper();
        String requestJson = objectMapper.writeValueAsString(requestBodyCaleg);

        MvcResult result = mockMvc.perform(post("/api/pemilu/post/caleg")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isCreated())
                .andReturn();

        String response = result.getResponse().getContentAsString();
        JsonNode jsonNode = objectMapper.readTree(response);
        String calegId = jsonNode.path("data").path("id").asText();

        // Hapus caleg berdasarkan ID
        mockMvc.perform(delete("/api/pemilu/delete/caleg/{id}", calegId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(200));

        // (Opsional) Pastikan data benar-benar tidak ditemukan
        mockMvc.perform(get("/api/pemilu/get/caleg/" + calegId))
                .andExpect(status().isNotFound());
    }
}