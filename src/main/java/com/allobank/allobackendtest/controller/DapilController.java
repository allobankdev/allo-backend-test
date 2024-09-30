package com.allobank.allobackendtest.controller;

import com.allobank.allobackendtest.model.Dapil;
import com.allobank.allobackendtest.service.DapilService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;



/**
 * Controller class for managing Dapil requests.
 */

@RestController
@RequestMapping("/api/dapils")
public class DapilController {
    private static final Logger logger = LoggerFactory.getLogger(DapilController.class);

    @Autowired
    private DapilService dapilService;


    /**
     * Retrieves all Dapil entities.
     *
     * @return a ResponseEntity containing a list of all Dapil entities and an HTTP status code.
     */

    @GetMapping
    public ResponseEntity<List<Dapil>> getAllDapils() {
        List<Dapil> dapils = dapilService.getAllDapils();
        return new ResponseEntity<>(dapils, HttpStatus.OK);
    }


    /**
     * Adds a new Dapil entity.
     *
     * @param dapil the Dapil entity to be added.
     * @return a ResponseEntity containing a success message and an HTTP status code.
     */

    @PostMapping
    public ResponseEntity<String> addDapil(@RequestBody Dapil dapil) {
        try {
            Dapil createdDapil = dapilService.addDapil(dapil);
            return new ResponseEntity<>("Dapil created successfully: " + createdDapil.getNamaDapil(), HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }


    /**
     * Retrieves a Dapil entity by its ID.
     *
     * @param id the ID of the Dapil entity to retrieve.
     * @return a ResponseEntity containing the Dapil entity if found, otherwise a 404 Not Found status.
     */

    @GetMapping("/{id}")
    public ResponseEntity<Dapil> getDapilById(@PathVariable Long id) {
        Optional<Dapil> dapil = dapilService.getDapilById(id);
        if (dapil.isPresent()) {
            return new ResponseEntity<>(dapil.get(), HttpStatus.OK);
        } else {
           return ResponseEntity.notFound().build();
        }
    }
}
