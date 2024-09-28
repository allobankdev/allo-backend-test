package com.allobank.allobackendtest.controller;

import com.allobank.allobackendtest.model.Caleg;
import com.allobank.allobackendtest.service.CalegService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/caleg")
public class CalegController {

    @Autowired
    private CalegService calegService;

    @GetMapping
    public Page<Caleg> getCaleg(
            @RequestParam(required = false) String namaDapil,
            @RequestParam(required = false) String namaPartai,
            @RequestParam(defaultValue = "nomorUrut") String sortBy,
            @RequestParam(defaultValue = "0") String pageNumber,
            @RequestParam(defaultValue = "10") String limit) {
        log.info("incoming request get data caleg");
        return calegService.getAllCaleg(namaDapil, namaPartai, sortBy, Integer.parseInt(pageNumber), Integer.parseInt(limit));
    }
}
