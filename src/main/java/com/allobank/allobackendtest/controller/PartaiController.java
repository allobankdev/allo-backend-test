package com.allobank.allobackendtest.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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
    public ResponseEntity<Partai> createPartai(@RequestBody Partai partai) {
        var createdPartai = partaiService.createPartai(partai);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdPartai);
    }

    @GetMapping
    public ResponseEntity<List<Partai>> getAllPartai() {
        return ResponseEntity.ok(partaiService.getAllPartai());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Partai> getPartaiById(@PathVariable UUID id) {
        return ResponseEntity.ok(partaiService.getPartaiById(id));
    }
}