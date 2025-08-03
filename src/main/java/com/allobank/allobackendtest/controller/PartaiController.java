package com.allobank.allobackendtest.controller;

import com.allobank.allobackendtest.dto.Request.PartaiRequestDTO;
import com.allobank.allobackendtest.dto.Response.PartaiResponseDTO;
import com.allobank.allobackendtest.response.ApiResponse;
import com.allobank.allobackendtest.service.PartaiService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/partai")
@RequiredArgsConstructor
public class PartaiController {

    private final PartaiService partaiService;

    @PostMapping
    public ResponseEntity<ApiResponse<PartaiResponseDTO>> create(@RequestBody PartaiRequestDTO request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponse<>(true, "Partai berhasil dibuat", partaiService.create(request)));
    }
}

