package com.allobank.allobackendtest.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.allobank.allobackendtest.exception.ResourceNotFoundException;
import com.allobank.allobackendtest.model.Caleg;
import com.allobank.allobackendtest.model.Dapil;
import com.allobank.allobackendtest.model.Partai;
import com.allobank.allobackendtest.repository.DapilRepository;
import com.allobank.allobackendtest.repository.PartaiRepository;
import com.allobank.allobackendtest.services.CalegService;

@RestController
@RequestMapping("api/caleg")
public class CalegContoller {

    @Autowired
    private CalegService calegService;

    @Autowired
    private DapilRepository dapilRepository;

    @Autowired
    private PartaiRepository partaiRepository;

    @GetMapping("/list")
    public ResponseEntity<?> getCaleg(
            @RequestParam(required = false) String dapil_id,
            @RequestParam(required = false) String partai_id,
            Pageable pageable) {
        try {

            Dapil dapil = null;
            if (dapil_id != null) {
                dapil = dapilRepository.findById(dapil_id)
                        .orElseThrow(() -> new ResourceNotFoundException("Dapil not found with id " + dapil_id));
            }
            // Cari partai jika ID disediakan
            Partai partai = null;
            if (partai_id != null) {
                partai = partaiRepository.findById(partai_id)
                        .orElseThrow(() -> new ResourceNotFoundException("Partai not found with id " + partai_id));
            }

            // Ambil data caleg dengan pagination
            Page<Caleg> calegPage;
            if (dapil != null && partai != null) {
                calegPage = calegService.getCalegByDapilAndPartai(dapil, partai, pageable);
            } else {
                calegPage = calegService.getAllCaleg(pageable); // Ambil semua caleg tanpa filter
            }
            // response 200 Ok
            return new ResponseEntity<>(calegPage, HttpStatus.OK);
        } catch (ResourceNotFoundException ex) {
            // Response dengan status 404 Not Found jika dapil atau partai tidak
            // ditemukan
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
        } catch (Exception ex) {
            // Response dengan status 500 Internal Server Error jika terjadi
            // kesalahan lainnya
            return new ResponseEntity<>("Internal Server Error", HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }
}
