package com.allobank.allobackendtest.controller;

import com.allobank.allobackendtest.model.ApiResponse;
import com.allobank.allobackendtest.model.Caleg;
import com.allobank.allobackendtest.service.CalegService;
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

    private final CalegService calegService;

    public CalegController(CalegService calegService) {
        this.calegService = calegService;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<Object>> listCalegs(
            @RequestParam(required = false) String namaDapil,
            @RequestParam(required = false) String namaPartai,
            @RequestParam(defaultValue = "asc") String sortOrder
    ) {

        List<Caleg> calegList = calegService.listCalegs(namaDapil, namaPartai,sortOrder);
        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.builder()
                .status(HttpStatus.OK.getReasonPhrase())
                .data(calegList)
                .message("Successfully List Caleg")
                .build());
    }
}
