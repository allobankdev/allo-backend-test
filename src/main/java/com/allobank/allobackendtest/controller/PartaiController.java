package com.allobank.allobackendtest.controller;

import com.allobank.allobackendtest.exception.PartaiNotFoundException;
import com.allobank.allobackendtest.model.Partai;
import com.allobank.allobackendtest.service.PartaiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST controller for managing political parties.
 */
@RestController
@RequestMapping("/api/partai")
public class PartaiController {

    private final PartaiService partaiService;

    @Autowired
    public PartaiController(PartaiService partaiService) {
        this.partaiService = partaiService;
    }

    /**
     * Retrieves all political parties.
     *
     * @return a list of all political parties.
     */
    @GetMapping
    public ResponseEntity<List<Partai>> getAllPartais() {
        List<Partai> partaiList = partaiService.getAllParties();
        return ResponseEntity.ok(partaiList);
    }

    /**
     * Adds a new partai.
     *
     * @param partai the Partai object to be added.
     * @return the saved Partai entity or error message if invalid data is provided.
     */
    @PostMapping
    public ResponseEntity<?> addPartai(@RequestBody Partai partai) {
        try {
            Partai savedPartai = partaiService.addPartai(partai);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedPartai);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }


    /**
     * Get a Partai by ID.
     * If the Partai is not found, return a custom message with HTTP status 404.
     *
     * @param id the ID of the Partai to retrieve
     * @return the Partai if found, otherwise an error response with a custom message.
     */
    @GetMapping("/{id}")
    public ResponseEntity<?> getPartaiById(@PathVariable Long id) {
        try {
            Partai partai = partaiService.getPartaiById(id);
            return ResponseEntity.ok(partai);
        } catch (PartaiNotFoundException e) {
            String message = "Partai with ID " + id + " is not registered, please register it first.";
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(message);
        }
    }
}
