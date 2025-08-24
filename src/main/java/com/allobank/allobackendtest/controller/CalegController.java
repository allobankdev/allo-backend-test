package com.allobank.allobackendtest.controller;

import com.allobank.allobackendtest.common.response.ApiResponse;
import com.allobank.allobackendtest.dto.CalegDto;
import com.allobank.allobackendtest.model.Caleg;
import com.allobank.allobackendtest.model.Partai;
import com.allobank.allobackendtest.service.CalegService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/caleg")
public class CalegController {

    private final CalegService service;

    public CalegController(CalegService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<Caleg>>> getAll(Pageable pageable) {
        Page<Caleg> page = service.getAll(pageable);

        ApiResponse<List<Caleg>> response = new ApiResponse<>(
                page.getContent(),
                "Successfully get data",
                new ApiResponse.Meta(page.getNumber(), page.getSize(), page.getTotalElements())
        );

        return ResponseEntity.ok(response); // <-- disini pakai ResponseEntity
    }


    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Caleg>> getById(@PathVariable UUID id) {
        Caleg caleg = service.getById(id);
        ApiResponse<Caleg> response = new ApiResponse<>(caleg, "Successfully find by id caleg",null);
        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<ApiResponse<Caleg>> create(@RequestBody @Valid CalegDto dto) {
        Caleg created = service.create(dto);
        ApiResponse<Caleg> response = new ApiResponse<>(created,"Successfull create caleg",null);
        return ResponseEntity.status(201).body(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<Caleg>> update(@PathVariable UUID id, @RequestBody @Valid CalegDto dto) {
        Caleg updated = service.update(id, dto);
        ApiResponse<Caleg> response = new ApiResponse<>(updated, "Successfully update caleg",null);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable UUID id) {
        service.delete(id);
        ApiResponse<Void> response = new ApiResponse<>(null,"Successfully Delete",null);
        return ResponseEntity.ok(response);
    }

    @GetMapping(params = "nama")
    public ResponseEntity<ApiResponse<List<Caleg>>> getByNama(@RequestParam String nama) {
        List<Caleg> caleg = service.getByNama(nama);
        ApiResponse<List<Caleg>> response = new ApiResponse<>(caleg,"Successfully find Caleg",null);
        return ResponseEntity.ok(response);
    }
}