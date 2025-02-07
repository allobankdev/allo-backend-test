package com.allobank.allobackendtest.controller;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.allobank.allobackendtest.dto.CalegDto;
import com.allobank.allobackendtest.model.Caleg;
import com.allobank.allobackendtest.service.CalegService;

@RestController
@RequestMapping("/api/caleg")
public class CalegController {
  @Autowired
  private CalegService calegService;

  @GetMapping
  public Page<CalegDto> getAllCaleg(
      @RequestParam(value = "page", defaultValue = "0") int page,
      @RequestParam(value = "size", defaultValue = "10") int size,
      @RequestParam(value = "sort", defaultValue = "nama,asc") String sort) {

    Sort.Direction direction = sort.endsWith(",desc") ? Sort.Direction.DESC : Sort.Direction.ASC;
    String[] properties = sort.substring(0, sort.lastIndexOf(",")).split(",");
    Sort sortBy = Sort.by(direction, properties);

    org.springframework.data.domain.Pageable pageable = org.springframework.data.domain.PageRequest.of(page, size,
        sortBy);

    Page<Caleg> calegPage = calegService.getAllCaleg(pageable);
    return calegPage.map(calegService::convertToDto);
  }

  @PostMapping
  public ResponseEntity<CalegDto> createCaleg(@Valid @RequestBody CalegDto calegDto) {
    Caleg caleg = calegService.convertToEntity(calegDto);
    Caleg createdCaleg = calegService.createCaleg(caleg);
    // System.out.println(createdCaleg);
    CalegDto createdCalegDto = calegService.convertToDto(createdCaleg);
    return new ResponseEntity<>(createdCalegDto, HttpStatus.CREATED);
  }

  @GetMapping("/filter")
  public List<CalegDto> getCalegByDapilAndPartai(
      @RequestParam("dapilId") String dapilId, // Changed to String
      @RequestParam("partaiId") String partaiId) { // Changed to String

    UUID dapilUUID = UUID.fromString(dapilId);
    UUID partaiUUID = UUID.fromString(partaiId);

    List<Caleg> calegList = calegService.getCalegByDapilAndPartai(dapilUUID, partaiUUID);
    return calegList.stream().map(calegService::convertToDto).collect(Collectors.toList());
  }

  @GetMapping("/sorted")
  public List<CalegDto> getCalegSortedByNomorUrut(
      @RequestParam(value = "direction", defaultValue = "asc") String direction) {

    Sort.Direction sortDirection = direction.equalsIgnoreCase("desc") ? Sort.Direction.DESC : Sort.Direction.ASC;

    List<Caleg> calegList = calegService.getCalegSortedByNomorUrut(sortDirection);
    return calegList.stream().map(calegService::convertToDto).collect(Collectors.toList());
  }
}
