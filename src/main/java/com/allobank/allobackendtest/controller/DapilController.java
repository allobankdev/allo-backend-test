package com.allobank.allobackendtest.controller;

import com.allobank.allobackendtest.model.Dapil;
import com.allobank.allobackendtest.service.DapilService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api")
public class DapilController {

    @Autowired
    private DapilService dapilService;

    @GetMapping("/dapil")
    public ResponseEntity<List<Dapil>> getAllDapil() {
        List<Dapil> dapilList = dapilService.getAllDapil();
        return new ResponseEntity<>(dapilList, HttpStatus.OK);
    }

    @PostMapping("/dapil")
    public ResponseEntity<Dapil> createDapil(@RequestBody Dapil dapil) {
        Dapil createdDapil = dapilService.createDapil(dapil);
        return new ResponseEntity<>(createdDapil, HttpStatus.CREATED);
    }

    @PutMapping("/dapil/{id}")
    public ResponseEntity<Dapil> updateDapil(@PathVariable("id") UUID id, @RequestBody Dapil dapil) {
        Dapil updatedDapil = dapilService.updateDapil(id, dapil);
        return new ResponseEntity<>(updatedDapil, HttpStatus.OK);
    }
}

