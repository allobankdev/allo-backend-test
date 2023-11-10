package com.allobank.allobackendtest.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.allobank.allobackendtest.model.BaseApiRes;
import com.allobank.allobackendtest.model.Caleg;
import com.allobank.allobackendtest.service.CalegService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("/calegs")
public class CalegController {

  private final CalegService calegService;
  
  @GetMapping("")
  ResponseEntity<BaseApiRes<List<Caleg>>> getCalegList(
    @RequestParam(required = false) String namaDapil,
    @RequestParam(required = false) String namaPartai,
    @RequestParam(required = false) String sortBy) {
      
    List<Caleg> calegList = calegService.getCalegListByDapilAndPartai(namaDapil, namaPartai);
    if (sortBy != null && sortBy.equals("nomorUrut"))
      calegList.sort((o1, o2) -> o1.getNomorUrut() - o2.getNomorUrut());
    return ResponseEntity.ok(new BaseApiRes<>(calegList, null));
  }

}
