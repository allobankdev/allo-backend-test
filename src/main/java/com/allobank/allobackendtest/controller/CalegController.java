package com.allobank.allobackendtest.controller;

import com.allobank.allobackendtest.model.Caleg;
import com.allobank.allobackendtest.service.CalegService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
public class CalegController {

    @Autowired
    private CalegService calegService;

    @GetMapping("/calegs")
    public List<Caleg> getCalegs(
            @RequestParam(required = false) UUID dapilId,
            @RequestParam(required = false) UUID partaiId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return calegService.getAllCalegs(dapilId, partaiId, page, size);
    }
}
