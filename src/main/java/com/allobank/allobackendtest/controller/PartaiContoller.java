package com.allobank.allobackendtest.controller;

import com.allobank.allobackendtest.service.PartaiService;
import com.allobank.allobackendtest.util.Response;
import com.allobank.allobackendtest.util.dto.PartaiRequestDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/pemilu")
public class PartaiContoller {

    @Autowired
    private PartaiService partaiService;

    @GetMapping("/all-partai")
    public ResponseEntity<Response> getAllPartai() {
        Response<Object> response = new Response<>();
        response.setMessage("Success fetch data");
        response.setData(partaiService.getAllPartai());
        response.setStatus(HttpStatus.OK.value());
        return ResponseEntity.status(HttpStatus.OK).contentType(MediaType.APPLICATION_JSON).body(response);
    }

    @PostMapping("/create-partai")
    public ResponseEntity<Response> createPartai(@RequestBody PartaiRequestDTO partai) {
        Response<Object> response = new Response<>();
        response.setMessage("Success create data");
        response.setData(partaiService.createPartai(partai));
        response.setStatus(HttpStatus.CREATED.value());
        return ResponseEntity.status(HttpStatus.CREATED).contentType(MediaType.APPLICATION_JSON).body(response);
    }

    @PatchMapping("/update-partai/{id}")
    public ResponseEntity<Response> updatePartai(@PathVariable("id") UUID id, @RequestBody PartaiRequestDTO partai) {
        Response<Object> response = new Response<>();
        response.setMessage("Success update data");
        response.setData(partaiService.updatePartai(id, partai));
        response.setStatus(HttpStatus.OK.value());
        return ResponseEntity.status(HttpStatus.OK).contentType(MediaType.APPLICATION_JSON).body(response);
    }

    @DeleteMapping("/remove-partai/{id}")
    public ResponseEntity<Response> deletePartai(@PathVariable("id") UUID id) {
        partaiService.deletePartai(id);

        Response<Object> response = new Response<>();
        response.setMessage("Success delete data");
        response.setData(null);
        response.setStatus(HttpStatus.OK.value());

        return ResponseEntity.status(HttpStatus.OK).contentType(MediaType.APPLICATION_JSON).body(response);
    }
}
