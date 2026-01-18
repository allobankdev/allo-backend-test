package com.example.allobank.backend.test.takehometest.controllers;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.allobank.backend.test.takehometest.dto.ResponseData;
import com.example.allobank.backend.test.takehometest.store.DataStore;

@RestController
@RequestMapping("/api/finance/data")
public class MainController {

    private final DataStore store;

    public MainController(DataStore store) {
        this.store = store;
    }

    @GetMapping("/{resourceType}")
    public ResponseEntity<Object> getData(
            @PathVariable String resourceType) {

        List<?> result = store.get(resourceType);
        ResponseData<Object> responseData = new ResponseData<>();

        if (result.isEmpty()) {
            responseData.setMessages("resourceType Not Found");
            responseData.setPayload(result);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(responseData);
        }
        responseData.setMessages("Success");
        responseData.setPayload(result);
        return ResponseEntity.ok(responseData);
    }
}
