package com.allobank.allobackendtest.controller;

import java.util.List;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.allobank.allobackendtest.dto.DapilDto;
import com.allobank.allobackendtest.model.Dapil;
import com.allobank.allobackendtest.service.DapilService;

@RestController
@RequestMapping("/api/dapil")
public class DapilController {
  @Autowired
  private DapilService dapilService;

  @GetMapping
  public List<DapilDto> getAllDapil() {
    List<Dapil> dapilList = dapilService.getAllDapil();
    return dapilList.stream().map(dapilService::convertToDto).collect(Collectors.toList());
  }

  @PostMapping
  public ResponseEntity<DapilDto> createDapil(@Valid @RequestBody DapilDto dapilDto) {
    Dapil dapil = dapilService.convertToEntity(dapilDto);
    Dapil createdDapil = dapilService.createDapil(dapil);
    DapilDto createdDapilDto = dapilService.convertToDto(createdDapil);
    return new ResponseEntity<>(createdDapilDto, HttpStatus.CREATED);
  }
}
