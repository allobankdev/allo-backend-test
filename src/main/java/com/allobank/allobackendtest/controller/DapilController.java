package com.allobank.allobackendtest.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.allobank.allobackendtest.model.Dapil;
import com.allobank.allobackendtest.service.DapilService;

@RestController
@RequestMapping("/api/v1/dapil")
public class DapilController {

    private final DapilService dapilService;

    public DapilController(DapilService dapilService) {
        this.dapilService = dapilService;
    }

    @PostMapping
    public ResponseEntity<?> createDapil(@RequestBody Dapil dapil) {
        dapilService.createDapil(dapil);
        return ResponseEntity.ok().build();
    }
}