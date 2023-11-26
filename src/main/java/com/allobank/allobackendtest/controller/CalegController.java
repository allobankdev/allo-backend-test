package com.allobank.allobackendtest.controller;

import com.allobank.allobackendtest.service.CalegService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.bind.DefaultValue;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CalegController {

    @Autowired
    CalegService calegServicel;

    @GetMapping("/caleg")
    public ResponseEntity<?> getCaleg(@RequestParam(name = "namaPartai", defaultValue = "") String namaPartai, @RequestParam(name = "namaDapil", defaultValue = "") String namaDapil) {
        try {
            return ResponseEntity.ok(calegServicel.getCalegList(namaPartai, namaDapil));
        } catch (Exception ex) {
            return ResponseEntity.status(500).body(ex.getMessage());
        }
    }
}
