package com.allobank.allobackendtest.controller;

import com.allobank.allobackendtest.model.dto.CalegDto;
import com.allobank.allobackendtest.service.CalegService;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/caleg")
public class CalegController {
    private final CalegService calegService;

    @Autowired
    public CalegController(CalegService calegService) {
        this.calegService = calegService;
    }

    @GetMapping
    public ResponseEntity<List<CalegDto>> getAllCaleg() {
        return new ResponseEntity<>(calegService.getCaleg(), HttpStatus.OK);
    }

    @GetMapping("/dapil/{nama_dapil}")
    public ResponseEntity<List<CalegDto>> getAllCalegByDapil(@PathVariable("nama_dapil") final String nama_dapil) {
        return new ResponseEntity<>(calegService.getCalegByDapil(nama_dapil), HttpStatus.OK);
    }

    @GetMapping("/partai/{nama_partai}")
    public ResponseEntity<List<CalegDto>> getAllCalegByPartai(@PathVariable("nama_partai") final String nama_partai) {
        return new ResponseEntity<>(calegService.getCalegByPartai(nama_partai), HttpStatus.OK);
    }
}
