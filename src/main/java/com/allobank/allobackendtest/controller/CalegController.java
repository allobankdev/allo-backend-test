package com.allobank.allobackendtest.controller;

import com.allobank.allobackendtest.dto.CalegDTO;
import com.allobank.allobackendtest.dto.CalegFilterDTO;
import com.allobank.allobackendtest.service.CalegService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/caleg")
@RequiredArgsConstructor
public class CalegController {

    private final CalegService calegService;

    @GetMapping
    public ResponseEntity<Page<CalegDTO>> getCalegList(
            @RequestParam(required = false) UUID dapilId,
            @RequestParam(required = false) UUID partaiId,
            @PageableDefault(sort = "nomorUrut", direction = Sort.Direction.ASC) Pageable pageable) {

        CalegFilterDTO filter = CalegFilterDTO.builder()
                .dapilId(dapilId)
                .partaiId(partaiId)
                .build();

        Page<CalegDTO> result = calegService.findAllWithFilter(filter, pageable);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CalegDTO> getCalegById(@PathVariable UUID id) {
        return calegService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}