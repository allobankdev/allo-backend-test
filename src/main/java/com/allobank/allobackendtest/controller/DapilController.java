package com.allobank.allobackendtest.controller;

import com.allobank.allobackendtest.dto.request.DapilRequestDTO;
import com.allobank.allobackendtest.dto.response.DapilResponseDTO;
import com.allobank.allobackendtest.dto.response.PaginatedResponseDTO;
import com.allobank.allobackendtest.payload.ApiResponse;
import com.allobank.allobackendtest.service.DapilService;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/dapil")
public class DapilController {

    @Autowired
    private DapilService dapilService;

    @GetMapping("")
    public ResponseEntity<ApiResponse<PaginatedResponseDTO<DapilResponseDTO>>> findAllDapil(
            @RequestParam(defaultValue = "0") int pageNo,
            @RequestParam(defaultValue = "3") int pageSize,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDirection){
        try {
            Page<DapilResponseDTO> dapilResponseDTOPage = dapilService.findAll(pageNo, pageSize, sortBy, sortDirection);
            PaginatedResponseDTO<DapilResponseDTO> paginatedResponseDTO = new PaginatedResponseDTO<>(dapilResponseDTOPage);

            ApiResponse<PaginatedResponseDTO<DapilResponseDTO>> successResponse =
                    new ApiResponse<>(HttpStatus.OK.value(),  HttpStatus.OK.getReasonPhrase(), paginatedResponseDTO);
            return ResponseEntity.status(HttpStatus.OK).body(successResponse);
        } catch (Exception e) {
            ApiResponse<PaginatedResponseDTO<DapilResponseDTO>> errorResponse =
                    new ApiResponse<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(), null);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> findDapilById(@PathVariable("id") UUID id){
        try {
            Optional<DapilResponseDTO> dapil = dapilService.findById(id);

            if(dapil.isPresent()){
                ApiResponse<DapilResponseDTO> successResponse =
                        new ApiResponse<>(HttpStatus.OK.value(),  HttpStatus.OK.getReasonPhrase(), dapil.get());
                return ResponseEntity.status(HttpStatus.OK).body(successResponse);
            } else {
                ApiResponse<DapilResponseDTO> notFoundResponse =
                        new ApiResponse<>(HttpStatus.NOT_FOUND.value(), HttpStatus.NOT_FOUND.getReasonPhrase(), null);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(notFoundResponse);
            }
        } catch (Exception e) {
            ApiResponse<DapilResponseDTO> errorResponse =
                    new ApiResponse<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(), null);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    @PostMapping("")
    public ResponseEntity<?> saveDapil(@Valid @RequestBody DapilRequestDTO dapilRequestDTO){
        try {
            DapilResponseDTO dapilResponseDTO = dapilService.save(dapilRequestDTO);

            ApiResponse<DapilResponseDTO> successResponse =
                    new ApiResponse<>(HttpStatus.OK.value(),  HttpStatus.OK.getReasonPhrase(), dapilResponseDTO);
            return  ResponseEntity.status(HttpStatus.OK).body(successResponse);
        } catch (EntityExistsException e) {
            ApiResponse<DapilResponseDTO> conflictResponse =
                    new ApiResponse<>(HttpStatus.CONFLICT.value(), e.getMessage(), null);
            return  ResponseEntity.status(HttpStatus.CONFLICT).body(conflictResponse);
        } catch (Exception e) {
            ApiResponse<DapilResponseDTO> errorResponse =
                    new ApiResponse<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(), null);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> updatePartai(@Valid @RequestBody DapilRequestDTO dapilRequestDTO, @PathVariable("id") UUID id) {
        try {
            DapilResponseDTO dapil = dapilService.update(dapilRequestDTO, id);

            ApiResponse<DapilResponseDTO> successResponse =
                    new ApiResponse<>(HttpStatus.OK.value(),  HttpStatus.OK.getReasonPhrase(), dapil);
            return ResponseEntity.status(HttpStatus.OK).body(successResponse);
        } catch (EntityExistsException e) {
            ApiResponse<DapilResponseDTO> conflictResponse =
                    new ApiResponse<>(HttpStatus.CONFLICT.value(), e.getMessage(), null);
            return  ResponseEntity.status(HttpStatus.CONFLICT).body(conflictResponse);
        } catch (EntityNotFoundException e) {
            ApiResponse<DapilResponseDTO> notFoundResponse =
                    new ApiResponse<>(HttpStatus.NOT_FOUND.value(), e.getMessage(), null);
            return  ResponseEntity.status(HttpStatus.NOT_FOUND).body(notFoundResponse);
        } catch (Exception e) {
            ApiResponse<DapilResponseDTO> errorResponse =
                    new ApiResponse<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(), null);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteDapil(@PathVariable("id") UUID id) {
        try {
            Optional<DapilResponseDTO> dapil = dapilService.findById(id);

            if(dapil.isEmpty()){
                ApiResponse<DapilResponseDTO> notFoundResponse =
                        new ApiResponse<>(HttpStatus.NOT_FOUND.value(), HttpStatus.NOT_FOUND.getReasonPhrase(), null);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(notFoundResponse);
            }

            dapilService.deleteById(id);
            ApiResponse<DapilResponseDTO> successResponseDTO =
                    new ApiResponse<>(HttpStatus.OK.value(),  HttpStatus.OK.getReasonPhrase(), dapil.get());
            return ResponseEntity.status(HttpStatus.OK).body(successResponseDTO);
        } catch (Exception e) {
            ApiResponse<DapilResponseDTO> errorResponse =
                    new ApiResponse<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(), null);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }
}
