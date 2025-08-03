package com.allobank.allobackendtest.controller;

import com.allobank.allobackendtest.model.Caleg;
import com.allobank.allobackendtest.repository.CalegRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*; // ← HARUS ADA
import static org.hamcrest.Matchers.is;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class testFilterCalegByPartai {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CalegRepository calegRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void testFilterCalegByPartai() throws Exception {
        Caleg c = new Caleg();
        c.setId("2");
        c.setNama("Siti");
        c.setNomorUrut(2);

        when(calegRepository.findByPartai_NamaPartai("Partai A")).thenReturn(List.of(c));
        mockMvc.perform(MockMvcRequestBuilders.get("/api/caleg/filter")
                .param("partai", "Partai A"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()", is(1)))
                .andExpect(jsonPath("$[0].nama", is("Siti")));
    }

}
