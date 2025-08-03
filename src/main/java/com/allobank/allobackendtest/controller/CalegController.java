package com.allobank.allobackendtest.controller;

import com.allobank.allobackendtest.dto.Request.CalegRequestDTO;
import com.allobank.allobackendtest.dto.Response.CalegResponseDTO;
import com.allobank.allobackendtest.response.ApiResponse;
import com.allobank.allobackendtest.service.CalegService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/caleg")
@RequiredArgsConstructor
public class CalegController {

    private final CalegService calegService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<CalegResponseDTO>>> getAllCaleg(
            @RequestParam(required = false) UUID dapilId,
            @RequestParam(required = false) UUID partaiId,
            @RequestParam(required = false, defaultValue = "nomorUrut") String sortBy
    ) {
        return ResponseEntity.ok(new ApiResponse<>(true, "Data caleg ditemukan", calegService.getAllCaleg(dapilId, partaiId, sortBy)));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<CalegResponseDTO>> getById(@PathVariable UUID id) {
        return ResponseEntity.ok(new ApiResponse<>(true, "Detail caleg ditemukan", calegService.getById(id)));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<CalegResponseDTO>> create(@RequestBody CalegRequestDTO request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponse<>(true, "Caleg berhasil ditambahkan", calegService.create(request)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<CalegResponseDTO>> update(@PathVariable UUID id, @RequestBody CalegRequestDTO request) {
        return ResponseEntity.ok(new ApiResponse<>(true, "Caleg berhasil diperbarui", calegService.update(id, request)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable UUID id) {
        calegService.delete(id);
        return ResponseEntity.ok(new ApiResponse<>(true, "Caleg berhasil dihapus", null));
    }
}
