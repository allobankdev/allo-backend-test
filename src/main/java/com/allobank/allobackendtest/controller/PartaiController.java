package com.allobank.allobackendtest.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.allobank.allobackendtest.model.Partai;
import com.allobank.allobackendtest.service.PartaiService;

@RestController
@RequestMapping("/api/v1/partai")
public class PartaiController {
    
    private final PartaiService partaiService;

    public PartaiController(PartaiService partaiService) {
        this.partaiService = partaiService;
    }

    @PostMapping
    public ResponseEntity<?> createPartai(@RequestBody Partai partai) {
        partaiService.createPartai(partai);
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity<?> getAllPartai() {
        return ResponseEntity.ok(partaiService.getAllPartai());
    }
}