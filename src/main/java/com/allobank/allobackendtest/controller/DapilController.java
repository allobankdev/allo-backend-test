package com.allobank.allobackendtest.controller;

import com.allobank.allobackendtest.common.response.ApiResponse;
import com.allobank.allobackendtest.dto.DapilDto;
import com.allobank.allobackendtest.model.Dapil;
import com.allobank.allobackendtest.service.DapilService;
import jakarta.validation.Valid;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/dapil")
public class DapilController {

    private static final Log log = LogFactory.getLog(DapilController.class);
    private final DapilService service;

    public DapilController(DapilService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<Dapil>>> getAll(Pageable pageable) {
        Page<Dapil> page = service.getAll(pageable);

        ApiResponse<List<Dapil>> response = new ApiResponse<>(
                page.getContent(),
                "Successfully get data",
                new ApiResponse.Meta(page.getNumber(), page.getSize(), page.getTotalElements())
        );

        return ResponseEntity.ok(response); 
    }


    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Dapil>> getById(@PathVariable UUID id) {
        Dapil dapil = service.getById(id);
        ApiResponse<Dapil> result = new ApiResponse<>(
                dapil,
                "Sucsessfull get data",null
        );
        return ResponseEntity.ok(result);
    }

    @PostMapping
    public ResponseEntity <ApiResponse<Dapil>> create(@RequestBody @Valid DapilDto dapil) {
        Dapil created = service.create(dapil);
        ApiResponse<Dapil> result = new ApiResponse<>(created,"Succcesfully Created Dapil",null);
        return ResponseEntity.status(201).body(result);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<Dapil>> update(@PathVariable UUID id, @RequestBody DapilDto dto) {
        Dapil updated = service.update(id,dto);
        ApiResponse<Dapil> result = new ApiResponse<>(updated, "Succesfully Update Dapil", null);
        return ResponseEntity.ok(result);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable UUID id) {
        service.delete(id);
        ApiResponse<Void> response = new ApiResponse<>(null,"Successfull Delete",null);
        return ResponseEntity.ok(response);
    }


    @GetMapping("/by-nama/{nama}")
    public ResponseEntity<ApiResponse<Dapil>> getByNama(@PathVariable String nama) {
        Dapil dapil = service.getByNama(nama);
        ApiResponse<Dapil> result = new ApiResponse<>(dapil, "Succefully get dapi",null);
        return ResponseEntity.ok(result);
    }
}