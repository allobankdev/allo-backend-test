package com.allobank.allobackendtest.controller;
import com.allobank.allobackendtest.model.Caleg;
import com.allobank.allobackendtest.repository.CalegRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;



@RestController
@RequestMapping("api/caleg")
public class CalegController {

    private CalegRepository calegRepository;

    @Autowired
    public CalegController(CalegRepository calegRepository) {
        this.calegRepository = calegRepository;
    }

    @GetMapping
    public ResponseEntity<List<Caleg>> getCalegs(@RequestParam(required = false) String dapil, @RequestParam(required = false) String partai, @RequestParam(required = false, defaultValue = "asc") String sortDirection ){
        Sort sort = Sort.by("nomorUrut");
        if (sortDirection.equalsIgnoreCase("desc")) {
            sort = sort.descending();
        }

        List<Caleg> result;

        if (dapil != null && partai != null) {
            result = calegRepository.findByDapilIdAndPartaiId(dapil, partai, sort);
        } else if (dapil != null) {
            result = calegRepository.findByDapilId(dapil, sort);
        } else if (partai != null) {
            result = calegRepository.findByPartaiId(partai, sort);
        } else {
            result = calegRepository.findAll(sort);
        }

        return ResponseEntity.ok(result);
    }


    @GetMapping("/filter")
    public ResponseEntity<List<Caleg>> getFilteredCaleg(
            @RequestParam(required = false) String dapilId,
            @RequestParam(required = false) String partaiId,
            @RequestParam(required = false, defaultValue = "asc") String sortDirection) {

        Sort sort = Sort.by("nomorUrut");
        if (sortDirection.equalsIgnoreCase("asc")) {
            sort = sort.descending();
        }

        List<Caleg> result;

        if (dapilId != null && partaiId != null) {
            result = calegRepository.findByDapilIdAndPartaiId(dapilId, partaiId, sort);
        } else if (dapilId != null) {
            result = calegRepository.findByDapilId(dapilId, sort);
        } else if (partaiId != null) {
            result = calegRepository.findByPartaiId(partaiId, sort);
        } else {
            result = calegRepository.findAll(sort);

        }


        return ResponseEntity.ok(result);
    }
}