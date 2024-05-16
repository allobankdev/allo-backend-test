package com.allobank.allobackendtest.controller;

import com.allobank.allobackendtest.model.Caleg;
import com.allobank.allobackendtest.service.CalegService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api")
public class CalegController {

    @Autowired
    private CalegService calegService;

    @GetMapping("/caleg")
    public ResponseEntity<?> getCaleg(@RequestParam(required = false) String partai,
                                @RequestParam(required = false) String dapil,
                                @RequestParam(required = false, defaultValue = "false") boolean sortByNomorUrut) {
        try {
            Map result = calegService.getCaleg(partai, dapil,sortByNomorUrut);
            if (result.get("success").equals(true)){
                return ResponseEntity.ok(result);
            }
            else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(result);
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }

    }

    @PostMapping("/caleg")
    public ResponseEntity<?> createCaleg(@RequestBody Caleg caleg) {
        try {
            Map result = calegService.createCaleg(caleg);
            if (result.get("success").equals(true)){
                return ResponseEntity.ok(result);
            }
            else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(result);
            }
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @PutMapping("/caleg/{id}")
    public ResponseEntity<?> updateCaleg(@PathVariable("id") UUID id, @RequestBody Caleg caleg) {
        try {
            Map result = calegService.updateCaleg(id,caleg);
            if (result.get("success").equals(true)){
                return ResponseEntity.ok(result);
            }
            else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(result);
            }
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
}

