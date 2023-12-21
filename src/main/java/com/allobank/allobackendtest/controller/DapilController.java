package com.allobank.allobackendtest.controller;

import com.allobank.allobackendtest.model.Dapil;
import com.allobank.allobackendtest.model.Partai;
import com.allobank.allobackendtest.service.DapilService;
import com.allobank.allobackendtest.utils.constant.ApiPathConstant;
import com.allobank.allobackendtest.utils.customeResponse.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(ApiPathConstant.DAPIL_PATH)
public class DapilController {

    @Autowired
    private DapilService dapilService;

    @GetMapping
    public List<Dapil> getAllDapils() {
        return dapilService.getAllDapils();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Dapil> getDapilById(@PathVariable String id) {
        Optional<Dapil> dapil = dapilService.getDapilById(id);
        return dapil.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PostMapping
    public ResponseEntity<Response<Dapil>> createDapil(@RequestBody Dapil dapil) {
        String message = String.format(ApiPathConstant.MESSAGE, "Dapil");
        Response<Dapil> response = new Response<>();
        response.setMessage(message);
        response.setData(dapilService.createDapil(dapil));
        return ResponseEntity.status(HttpStatus.CREATED)
                .contentType(MediaType.APPLICATION_JSON)
                .body(response);
    }


    @PutMapping("/{id}")
    public ResponseEntity<Object> updateDapil(@PathVariable String id, @RequestBody Dapil dapil) {
        try {
            Dapil updatedDapil = dapilService.updateDapil(id, dapil);
            return new ResponseEntity<>(updatedDapil, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteDapil(@PathVariable String id) {
        try {
            dapilService.deleteDapil(id);
            return new ResponseEntity<>("Dapil with ID " + id + " deleted successfully.", HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>("Dapil with ID " + id + " not found.", HttpStatus.NOT_FOUND);
        }
    }
}
