package com.allobank.allobackendtest.controller;

import com.allobank.allobackendtest.service.DapilService;
import com.allobank.allobackendtest.util.Response;
import com.allobank.allobackendtest.util.dto.DapilRequestDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/pemilu")
public class DapilController {

    @Autowired
    private DapilService dapilService;

    @GetMapping("/all-dapil")
    public ResponseEntity<Response> getDapil() {
        Response<Object> response = new Response<>();
        response.setMessage("Success get data");
        response.setData(dapilService.getAllDapil());
        response.setStatus(HttpStatus.OK.value());
        return ResponseEntity.status(HttpStatus.OK).contentType(MediaType.APPLICATION_JSON).body(response);
    }

    @PostMapping("/create-dapil")
    public ResponseEntity<Response> createDapil(@RequestBody DapilRequestDTO dapilRequestDTO) {
        Response<Object> response = new Response<>();
        response.setMessage("Success create data");
        response.setData(dapilService.createDapil(dapilRequestDTO));
        response.setStatus(HttpStatus.CREATED.value());
        return ResponseEntity.status(HttpStatus.CREATED).contentType(MediaType.APPLICATION_JSON).body(response);
    }

    @PatchMapping("/update-dapil/{id}")
    public ResponseEntity<Response> updateDapil(@PathVariable("id") UUID id, @RequestBody DapilRequestDTO dapil) {
        Response<Object> response = new Response<>();
        response.setMessage("Success update data");
        response.setData(dapilService.updateDapil(id, dapil));
        response.setStatus(HttpStatus.OK.value());

        return ResponseEntity.status(HttpStatus.OK).contentType(MediaType.APPLICATION_JSON).body(response);
    }

    @DeleteMapping("/remove-dapil/{id}")
    public ResponseEntity<Response> deleteDapil(@PathVariable("id") UUID id) {
        dapilService.deleteDapil(id);

        Response<Object> response = new Response<>();
        response.setMessage("Success delete data");
        response.setData(null);
        response.setStatus(HttpStatus.OK.value());
        return ResponseEntity.status(HttpStatus.OK).contentType(MediaType.APPLICATION_JSON).body(response);
    }
}
