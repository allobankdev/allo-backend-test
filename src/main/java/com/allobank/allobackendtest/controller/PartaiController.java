package com.allobank.allobackendtest.controller;

import com.allobank.allobackendtest.dto.request.PartaiRequestDTO;
import com.allobank.allobackendtest.dto.response.PaginatedResponseDTO;
import com.allobank.allobackendtest.dto.response.PartaiResponseDTO;
import com.allobank.allobackendtest.payload.ApiResponse;
import com.allobank.allobackendtest.service.PartaiService;
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
@RequestMapping("/api/partai")
public class PartaiController {
    @Autowired
    private PartaiService partaiService;

    @GetMapping("")
    public ResponseEntity<ApiResponse<PaginatedResponseDTO<PartaiResponseDTO>>> findAllPartai(
            @RequestParam(defaultValue = "0") int pageNo,
            @RequestParam(defaultValue = "3") int pageSize,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDirection){
        try {
            Page<PartaiResponseDTO> partaiResponseDTOPage = partaiService.findAll(pageNo, pageSize, sortBy, sortDirection);
            PaginatedResponseDTO<PartaiResponseDTO> paginatedResponse = new PaginatedResponseDTO<>(partaiResponseDTOPage);

            ApiResponse<PaginatedResponseDTO<PartaiResponseDTO>> successResponse =
                    new ApiResponse<>(HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase(), paginatedResponse);
            return ResponseEntity.status(HttpStatus.OK).body(successResponse);
        } catch (Exception e) {
            ApiResponse<PaginatedResponseDTO<PartaiResponseDTO>> errorResponse =
                    new ApiResponse<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(), null);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> findPartaiById(@PathVariable("id") UUID id){
        try {
            Optional<PartaiResponseDTO> partai =  partaiService.findById(id);

            if (partai.isPresent()){
                ApiResponse<PartaiResponseDTO> successReponse =
                        new ApiResponse<>(HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase(), partai.get());
                return ResponseEntity.status(HttpStatus.OK).body(successReponse);
            } else {
                ApiResponse<PartaiResponseDTO> notFoundResponse =
                        new ApiResponse<>(HttpStatus.NOT_FOUND.value(), HttpStatus.NOT_FOUND.getReasonPhrase(), null);
                return  ResponseEntity.status(HttpStatus.NOT_FOUND).body(notFoundResponse);
            }
        } catch (Exception e) {
            ApiResponse<PartaiResponseDTO> errorResponse =
                    new ApiResponse<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(), null);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    @PostMapping("")
    public ResponseEntity<?> savePartai(@Valid @RequestBody PartaiRequestDTO partaiRequestDTO){
        try {
            PartaiResponseDTO partai = partaiService.save(partaiRequestDTO);

            ApiResponse<PartaiResponseDTO> successResponse =
                    new ApiResponse<>(HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase(), partai);
            return  ResponseEntity.status(HttpStatus.OK).body(successResponse);
        } catch (EntityExistsException e) {
            ApiResponse<PartaiResponseDTO> conflictResponse =
                    new ApiResponse<>(HttpStatus.CONFLICT.value(), e.getMessage(), null);
            return ResponseEntity.status(HttpStatus.CONFLICT).body(conflictResponse);
        } catch (Exception e) {
            ApiResponse<PartaiResponseDTO> errorResponse =
                    new ApiResponse<>(HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST.getReasonPhrase(), null);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> updatePartai(@Valid @RequestBody PartaiRequestDTO partaiRequestDTO, @PathVariable("id") UUID id){
        try {
            PartaiResponseDTO partai = partaiService.update(partaiRequestDTO, id);

            ApiResponse<PartaiResponseDTO> successResponse =
                    new ApiResponse<>(HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase(), partai);
            return  ResponseEntity.status(HttpStatus.OK).body(successResponse);
        } catch (EntityExistsException e) {
            ApiResponse<PartaiResponseDTO> conflictResponse =
                    new ApiResponse<>(HttpStatus.CONFLICT.value(), e.getMessage(), null);
            return ResponseEntity.status(HttpStatus.CONFLICT).body(conflictResponse);
        } catch (EntityNotFoundException e) {
            ApiResponse<PartaiResponseDTO> notFoundResponse =
                    new ApiResponse<>(HttpStatus.NOT_FOUND.value(), e.getMessage(), null);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(notFoundResponse);
        } catch (Exception e) {
            ApiResponse<PartaiResponseDTO> errorResponse =
                    new ApiResponse<>(HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST.getReasonPhrase(), null);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deletePartai(@PathVariable("id") UUID id){
        try {
            Optional<PartaiResponseDTO> partai = partaiService.findById(id);

            if (partai.isEmpty()){
                ApiResponse<PartaiResponseDTO> notFoundResponse =
                        new ApiResponse<>(HttpStatus.NOT_FOUND.value(), HttpStatus.NOT_FOUND.getReasonPhrase(), null);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(notFoundResponse);
            }

            partaiService.deleteById(id);
            ApiResponse<PartaiResponseDTO> successReponse =
                    new ApiResponse<>(HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase(),null);
            return  ResponseEntity.status(HttpStatus.OK).body(successReponse);
        } catch (Exception e) {
            ApiResponse<PartaiResponseDTO> errorResponse =
                    new ApiResponse<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(), null);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }


}
