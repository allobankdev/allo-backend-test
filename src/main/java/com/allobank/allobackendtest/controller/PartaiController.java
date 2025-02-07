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

import com.allobank.allobackendtest.dto.PartaiDto;
import com.allobank.allobackendtest.model.Partai;
import com.allobank.allobackendtest.service.PartaiService;

@RestController
@RequestMapping("/api/partai")
public class PartaiController {
  @Autowired
  private PartaiService partaiService;

  @GetMapping
  public List<PartaiDto> getAllPartai() {
    List<Partai> partaiList = partaiService.getAllPartai();
    return partaiList.stream().map(partaiService::convertToDto).collect(Collectors.toList());
  }

  @PostMapping
  public ResponseEntity<PartaiDto> createPartai(@Valid @RequestBody PartaiDto partaiDto) {
    Partai partai = partaiService.convertToEntity(partaiDto);
    Partai createdPartai = partaiService.createPartai(partai);
    PartaiDto createdPartaiDto = partaiService.convertToDto(createdPartai);
    return new ResponseEntity<>(createdPartaiDto, HttpStatus.CREATED);
  }
}
