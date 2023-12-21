package com.allobank.allobackendtest.controller;

import com.allobank.allobackendtest.model.Caleg;
import com.allobank.allobackendtest.model.GetCalegRequest;
import com.allobank.allobackendtest.model.WebResponse;
import com.allobank.allobackendtest.service.CalegService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    public WebResponse<List<Caleg>> getAllCaleg(
            @RequestParam(name = "dapil", required = false) String dapil,
            @RequestParam(name = "partai", required = false) String partai,
            @RequestParam(name = "sort", defaultValue = "nomorUrut") String sort) {

        GetCalegRequest request = GetCalegRequest
                .builder()
                .dapil(dapil)
                .partai(partai)
                .sort(sort)
                .build();
        List<Caleg> calegList = calegService.getAllCaleg(request);
        return WebResponse.<List<Caleg>>builder().data(calegList).build();
    }
}
