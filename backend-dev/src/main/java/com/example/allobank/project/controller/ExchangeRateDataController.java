package com.example.allobank.project.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.allobank.project.service.IDRDataServices;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/finance/data")
@RequiredArgsConstructor
public class ExchangeRateDataController {
	
	@Autowired
	IDRDataServices idrDataServices;

    @GetMapping("/{resourceType}")
    public ResponseEntity<?> getData(@PathVariable String resourceType) {
        Object data = idrDataServices.getData(resourceType);
        return ResponseEntity.ok(data);
    }
}
