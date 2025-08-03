package com.allobank.allobackendtest.controller;

import com.allobank.allobackendtest.dto.Request.DapilRequestDTO;
import com.allobank.allobackendtest.dto.Response.DapilResponseDTO;
import com.allobank.allobackendtest.response.ApiResponse;
import com.allobank.allobackendtest.service.DapilService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/dapil")
@RequiredArgsConstructor
public class DapilController {

    private final DapilService dapilService;

    @PostMapping
    public ResponseEntity<ApiResponse<DapilResponseDTO>> create(@RequestBody DapilRequestDTO request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponse<>(true, "Dapil berhasil dibuat", dapilService.create(request)));
    }
}