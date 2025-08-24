package com.allobank.allobackendtest.controller;

import com.allobank.allobackendtest.common.response.ApiResponse;
import com.allobank.allobackendtest.dto.CalegDto;
import com.allobank.allobackendtest.dto.PartaiDto;
import com.allobank.allobackendtest.entity.CalegEntity;
import com.allobank.allobackendtest.model.Caleg;
import com.allobank.allobackendtest.model.Partai;
import com.allobank.allobackendtest.repository.CalegRepository;
import com.allobank.allobackendtest.service.PartaiService;
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
@RequestMapping("/partai")
public class PartaiController {
    private final PartaiService service;
    public  PartaiController(PartaiService service){
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<Partai>>> getAll(Pageable pageable) {
        Page<Partai> page = service.getAll(pageable);

        ApiResponse<List<Partai>> response = new ApiResponse<>(
                page.getContent(),
                "Successfully get data",
                new ApiResponse.Meta(page.getNumber(), page.getSize(), page.getTotalElements())
        );

        return ResponseEntity.ok(response);
    }



    @PostMapping
    public ResponseEntity<ApiResponse<Partai>> create(@RequestBody @Valid PartaiDto dto) {
        Partai created = service.create(dto);
        ApiResponse<Partai> result = new ApiResponse<>(created, "Successfully create Partai",null);
        return ResponseEntity.status(201).body(result);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<Partai>> update(@PathVariable UUID id, @RequestBody PartaiDto dto){
        Partai update = service.update(id,dto);
        ApiResponse<Partai> resutl = new ApiResponse<>(update, "Successfully update Partai",null);
        return ResponseEntity.ok(resutl);
    }

    @DeleteMapping("/{id}")
    public  ResponseEntity<ApiResponse<Void>> delete(@PathVariable UUID id){
        service.delete(id);
        ApiResponse<Void> response = new ApiResponse<>(null, "Successfully Delete Partai",null);
        return ResponseEntity.ok(response);
    }
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Partai>> getById(@PathVariable UUID id){
        Partai partai = service.getById(id);
        ApiResponse<Partai> response = new ApiResponse<>(partai,"Successfulyy find partai by id",null);
        return ResponseEntity.ok(response);
    }
    @GetMapping(params = "nama")
    public  ResponseEntity<ApiResponse<Partai>> getBYNamaPartai(String nama){
        Partai partai = service.findByNamaPartai(nama);
        ApiResponse<Partai> response = new ApiResponse<>(partai, "Successfully find partai by name",null);
        return ResponseEntity.ok(response);
    }
}
