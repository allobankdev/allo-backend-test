package com.allobank.allobackendtest.controller;

import com.allobank.allobackendtest.dto.Response.CalegResponseDTO;
import com.allobank.allobackendtest.entity.JenisKelaminEnum;
import com.allobank.allobackendtest.response.ApiResponse;
import com.allobank.allobackendtest.service.CalegService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.UUID;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CalegController.class)
class CalegControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CalegService calegService;

    @Test
    void testGetAllCaleg() throws Exception {
        UUID dapilId = UUID.randomUUID();
        UUID partaiId = UUID.randomUUID();

        CalegResponseDTO responseDTO = new CalegResponseDTO();
        responseDTO.setId(UUID.randomUUID());
        responseDTO.setNama("Agus");
        responseDTO.setNomorUrut(1);
        responseDTO.setJenisKelamin(JenisKelaminEnum.LAKILAKI);

        Mockito.when(calegService.getAllCaleg(dapilId, partaiId, "nomorUrut"))
                .thenReturn(List.of(responseDTO));

        try {
            mockMvc.perform(get("/api/caleg")
                            .param("dapilId", dapilId.toString())
                            .param("partaiId", partaiId.toString())
                            .param("sortBy", "nomorUrut")
                            .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.success", is(true)))
                    .andExpect(jsonPath("$.data", hasSize(1)))
                    .andExpect(jsonPath("$.data[0].nama", is("Agus")));

            System.out.println("✅ testGetAllCaleg: passed");
        } catch (AssertionError | Exception e) {
            System.out.println("❌ testGetAllCaleg: failed");
            throw e;
        }
    }
}