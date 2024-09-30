package com.allobank.allobackendtest.controller;

import com.allobank.allobackendtest.dto.CalegRequest;
import com.allobank.allobackendtest.exception.EntityNotFoundException;
import com.allobank.allobackendtest.service.CalegService;
import com.allobank.allobackendtest.service.DapilService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;



/**
 * Controller class for handling Caleg-related requests.
 */
@RestController
@RequestMapping("/api/caleg")
public class CalegController {

    @Autowired
    private CalegService calegService;

    @Autowired
    private DapilService dapilService;



    /**
     * Retrieves all Caleg based on optional filters for Dapil name, Partai name,
     * and sorting direction, along with pagination.
     *
     * @param namaDapil    Optional name of the Dapil for filtering Caleg
     * @param namaPartai   Optional name of the Partai for filtering Caleg
     * @param sortDirection Optional sorting direction, "ASC" or "DESC"
     * @param pageable     Pageable object for pagination
     * @return ResponseEntity containing the list of Caleg or an error message
     */
    @GetMapping
    public ResponseEntity<?> getAllCalegs(@RequestParam(name = "namaDapil", required = false) String namaDapil,
                                    @RequestParam(name = "namaPartai", required = false) String namaPartai,
                                    @RequestParam(name = "sortDirection", required = false) String sortDirection,
                                    Pageable pageable) {

        return ResponseEntity.ok(calegService.getAllCalegs(namaDapil,namaPartai,sortDirection,pageable));
    }


    /**
     * Adds a new Caleg based on the provided CalegRequest object.
     *
     * @param request CalegRequest object containing the new Caleg data
     * @return ResponseEntity containing the created Caleg or an error message
     */
    @PostMapping
    public ResponseEntity<?> addCaleg(@RequestBody CalegRequest request) {
        try {
            return ResponseEntity.status(HttpStatus.CREATED).body(calegService.addCaleg(request));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("Input Error: " + e.getMessage());
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Not Found: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An unexpected error occurred.");
        }
    }

}
