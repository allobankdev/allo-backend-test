package com.allobank.allobackendtest.controller;


import com.allobank.allobackendtest.model.Caleg;
import com.allobank.allobackendtest.repository.CalegRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Sort;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.mockito.ArgumentMatchers.any;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@WebMvcTest (CalegController.class)
public class CalegControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private CalegRepository calegRepository;
    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void testGetAllCaleg() throws Exception{
        Caleg c = new Caleg();
        c.setId("1");
        c.setNama("Budi");
        c.setNomorUrut(1);

        when(calegRepository.findAll(any(Sort.class))).thenReturn(Arrays.asList(c));

        mockMvc.perform(MockMvcRequestBuilders.get("/api/caleg"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()", is(1)))
                .andExpect(jsonPath("$[0].nama", is("Budi")));


    }
}
