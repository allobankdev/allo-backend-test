package com.allobank.allobackendtest.controller;

import com.allobank.allobackendtest.model.Caleg;
import com.allobank.allobackendtest.service.CalegService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class CalegController {

    @Autowired
    private CalegService calegService;

    @GetMapping("/caleg")
    public List<Caleg> getCalegList(
        @RequestParam(required = false) Long dapilId,
        @RequestParam(required = false) Long partaiId,
        @RequestParam(defaultValue = "nomor_urut") String sortBy
    ) {
        if (dapilId != null && partaiId != null) {
            return calegService.getFilteredCaleg(dapilId, partaiId, sortBy);
        }
        // Providing default sorting parameter if none is provided
        return calegService.getAllCaleg(sortBy);
    }
}