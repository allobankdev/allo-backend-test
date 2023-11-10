package com.allobank.allobackendtest;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import lombok.extern.log4j.Log4j2;

@SpringBootTest
@AutoConfigureMockMvc
@Log4j2
public class CalegMockingTests {

  @Autowired
  private MockMvc mockMvc;

  @Test
  void testMockController_getCalegByDapilAndPartai_whenInitWithNoParams_returnListOfSize3() throws Exception {
    log.info("Mock Testing 1");
    log.info("testMockController_getCalegByDapilAndPartai_whenInitWithNoParams_returnListOfSize3");
    mockMvc.perform(get("/calegs")
      .contentType(MediaType.APPLICATION_JSON)
      .accept(MediaType.APPLICATION_JSON))
      .andExpectAll(
        status().isOk(),
        jsonPath("$.data").isArray(),
        jsonPath("$.data.length()").value(3),
        jsonPath("$.error").isEmpty(),
        jsonPath("$.data[0].dapil.namaDapil").value("Nama_Dapil_1")
      );
  }

  @Test
  void testMockController_getCalegByDapilAndPartai_whenInitWithDapilNameAndPartaiName_returnListOfSize1() throws Exception {
    log.info("Mock Testing 1");
    log.info("testMockController_getCalegByDapilAndPartai_whenInitWithDapilNameAndPartaiName_returnListOfSize1");
    mockMvc.perform(get("/calegs")
      .param("namaDapil", "Nama_Dapil_2")
      .param("namaPartai", "Nama_Partai_2")
      .contentType(MediaType.APPLICATION_JSON)
      .accept(MediaType.APPLICATION_JSON))
      .andExpectAll(
        status().isOk(),
        jsonPath("$.data").isArray(),
        jsonPath("$.data.length()").value(1),
        jsonPath("$.error").isEmpty(),
        jsonPath("$.data[0].dapil.namaDapil").value("Nama_Dapil_2"),
        jsonPath("$.data[0].partai.namaPartai").value("Nama_Partai_2")
      );
  }
  
}
