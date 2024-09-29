package com.allobank.allobackendtest.controller;

import com.allobank.allobackendtest.dto.CalegResponseDto;
import com.allobank.allobackendtest.service.CalegService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class CalegController {

    @Autowired
    private CalegService calegService;

    @GetMapping("/api/caleg")
    public ResponseEntity<List<CalegResponseDto>> getCaleg(@RequestParam(name = "dapil",required = false) String dapil,
                                                           @RequestParam(name = "partai",required = false) String partai) {

        return new ResponseEntity<>(calegService.getListCaleg(dapil, partai), HttpStatus.OK);
    }
}
