package com.allobank.allobackendtest.controller;

import com.allobank.allobackendtest.model.Partai;
import com.allobank.allobackendtest.service.PartaiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api")
public class PartaiController {

    @Autowired
    private PartaiService partaiService;

    @GetMapping("/partai")
    public ResponseEntity<List<Partai>> getAllPartai() {
        List<Partai> partailist = partaiService.getAllPartai();
        return new ResponseEntity<>(partailist, HttpStatus.OK);
    }

    @PostMapping("/partai")
    public ResponseEntity<Partai> createPartai(@RequestBody Partai partai) {
        Partai createdPartai = partaiService.createPartai(partai);
        return new ResponseEntity<>(createdPartai, HttpStatus.CREATED);
    }

    @PutMapping("/partai/{id}")
    public ResponseEntity<Partai> updatePartai(@PathVariable("id") UUID id, @RequestBody Partai partai) {
        Partai updatedPartai = partaiService.updatePartai(id, partai);
        return new ResponseEntity<>(updatedPartai, HttpStatus.OK);
    }
}

