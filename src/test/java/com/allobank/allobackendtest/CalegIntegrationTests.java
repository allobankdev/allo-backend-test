package com.allobank.allobackendtest;

import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import com.allobank.allobackendtest.model.BaseApiRes;
import com.allobank.allobackendtest.model.Caleg;
import com.allobank.allobackendtest.service.CalegService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.log4j.Log4j2;

@SpringBootTest
@AutoConfigureMockMvc
@Log4j2
public class CalegIntegrationTests {
  
  @Autowired
  private CalegService calegService;
  
  @Test
  void testService_getCalegByDapilAndPartai_whenInitWithDapilNameAndPartaiName_returnListOfSize1() {
    log.info("Integration Test 1");
    log.info("testService_getCalegByDapilAndPartai_whenInitWithDapilNameAndPartaiName_returnListOfSize1");
    List<Caleg> calegList = calegService.getCalegListByDapilAndPartai("Nama_Dapil_1", "Nama_Partai_1");
    Assertions.assertEquals(1, calegList.size());
  }

  @Test
  void testController_getCalegList_whenInitWithDapilNameAndPartaiName_returnListOfSize1() {
    log.info("Integration Test 2");
    log.info("testController_getCalegList_whenInitWithDapilNameAndPartaiName_returnListOfSize1");
    RestTemplate restTemplate = new RestTemplate();
    ResponseEntity<BaseApiRes<List<Caleg>>> res = restTemplate.getForEntity(
      "http://localhost:8080/calegs?namaDapil=Nama_Dapil_1&namaPartai=Nama_Partai_1", 
      null, new TypeReference<BaseApiRes<List<Caleg>>>() {});
    try {
      ObjectMapper objMapper = new ObjectMapper();
      BaseApiRes<List<Caleg>> baseApiRes = objMapper.readValue(
          res.getBody().toString(), new TypeReference<BaseApiRes<List<Caleg>>>() {
          });
      List<Caleg> calegList = baseApiRes.getData();
      Assertions.assertEquals(1, calegList.size());
    } catch (Exception ex) {}
  }

  @Test
  void testController_getCalegList_whenInitWithSortByNomorUrut_returnAscendingSortedListByNomorUrut() {
    log.info("Integration Test 3");
    log.info("testController_getCalegList_whenInitWithSortByNomorUrut_returnAscendingSortedListByNomorUrut");
    RestTemplate restTemplate = new RestTemplate();
    ResponseEntity<BaseApiRes<List<Caleg>>> res = restTemplate.getForEntity(
      "http://localhost:8080/calegs?sortBy=nomorUrut", 
      null, new TypeReference<BaseApiRes<List<Caleg>>>() {});
    try {
      ObjectMapper objMapper = new ObjectMapper();
      BaseApiRes<List<Caleg>> baseApiRes = objMapper.readValue(
          res.getBody().toString(), new TypeReference<BaseApiRes<List<Caleg>>>() {
          });
      List<Caleg> calegList = baseApiRes.getData();
      Assertions.assertTrue(
        calegList.get(0).getNomorUrut() < calegList.get(1).getNomorUrut()
        );
    } catch (Exception ex) {}
  }

}
