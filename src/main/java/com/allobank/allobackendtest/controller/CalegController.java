package com.allobank.allobackendtest.controller;


import com.allobank.allobackendtest.model.Caleg;
import com.allobank.allobackendtest.service.CalegService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/caleg")
public class CalegController {

    @Autowired
    private CalegService calegService;

    @GetMapping
    public List<Caleg> getCaleg(
            @RequestParam(required = false) String dapilId,
            @RequestParam(required = false) String partaiId,
            @RequestParam(defaultValue = "nomorUrut") String sortBy) {
        return calegService.getAllCaleg(dapilId, partaiId, sortBy);
    }
}
