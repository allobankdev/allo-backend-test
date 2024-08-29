package com.allobank.allobackendtest.controller;

import com.allobank.allobackendtest.model.Caleg;
import com.allobank.allobackendtest.model.ErrorResponse;
import com.allobank.allobackendtest.service.CalegService;
import com.allobank.allobackendtest.exception.CalegNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/calegs")
public class CalegController {

    @Autowired
    private CalegService calegService;

    @GetMapping("/all")
    public ResponseEntity<?> getAllCalegs(
            @RequestParam(value = "namaDapil", required = false) String namaDapil,
            @RequestParam(value = "namaPartai", required = false) String namaPartai,
            @RequestParam(value = "page", defaultValue = "0") Integer page,
            @RequestParam(value = "size", defaultValue = "10") Integer size,
            @RequestParam(value = "sortBy", defaultValue = "nomorUrut") String sortBy) {
        try {
            List<Caleg> calegs = calegService.getAllCalegs(namaDapil, namaPartai, page, size, sortBy);
            return ResponseEntity.ok(calegs);
        } catch (CalegNotFoundException e) {
            ErrorResponse errorResponse = new ErrorResponse("Caleg tidak ditemukan");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
        }
    }
}