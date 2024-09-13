package com.allobank.allobackendtest.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.allobank.allobackendtest.model.Caleg;
import com.allobank.allobackendtest.service.CalegService;

@RestController
@RequestMapping("/api/v1/caleg")
public class CalegController {
    
    
    private final CalegService calegService;

    @Autowired
    public CalegController(CalegService calegService) {
        this.calegService = calegService;
    }

    @GetMapping
    public List<Caleg> getCalegs(
        @RequestParam(required = false) UUID dapilId,
        @RequestParam(required = false) UUID partaiId,
        @RequestParam(required = false) String sortBy
    ) {
        return calegService.getCalegs(dapilId, partaiId, sortBy);
    }

    @PostMapping
    public ResponseEntity<?> createCaleg(@RequestBody Caleg caleg) {
        calegService.createCaleg(caleg);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{id}")
    public Caleg getCaleg(@PathVariable UUID id) {
        return calegService.getCalegById(id);
    }
    
}