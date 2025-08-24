package com.allobank.allobackendtest.controller;

import com.allobank.allobackendtest.dto.request.CalegRequestDTO;
import com.allobank.allobackendtest.dto.response.CalegResponseDTO;
import com.allobank.allobackendtest.dto.response.DapilResponseDTO;
import com.allobank.allobackendtest.dto.response.PaginatedResponseDTO;
import com.allobank.allobackendtest.payload.ApiResponse;
import com.allobank.allobackendtest.service.CalegService;
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
@RequestMapping("/api/caleg")
public class CalegController {

    @Autowired
    private CalegService calegService;

    @GetMapping("")
    public ResponseEntity<ApiResponse<PaginatedResponseDTO<CalegResponseDTO>>> findAllCaleg(
            @RequestParam(required = false) String namaPartai,
            @RequestParam(required = false) String namaDapil,
            @RequestParam(defaultValue = "0") int pageNo,
            @RequestParam(defaultValue = "3") int pageSize,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDirection){
        try {
            Page<CalegResponseDTO> calegResponseDTOPage = calegService.findAll(namaPartai, namaDapil, pageNo, pageSize, sortBy, sortDirection);
            PaginatedResponseDTO<CalegResponseDTO> paginatedResponse = new PaginatedResponseDTO<>(calegResponseDTOPage);

            ApiResponse<PaginatedResponseDTO<CalegResponseDTO>> successResponse =
                    new ApiResponse<>(HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase(), paginatedResponse);
            return ResponseEntity.status(HttpStatus.OK).body(successResponse);
        } catch (Exception ex) {
            ApiResponse<PaginatedResponseDTO<CalegResponseDTO>> errorResponse =
                    new ApiResponse<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(), null);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> findCalegById(@PathVariable("id") UUID id){
        try{
            Optional<CalegResponseDTO> calegResponseDTO = calegService.findById(id);

            if (calegResponseDTO.isPresent()){
                ApiResponse<CalegResponseDTO> successResponse =
                        new ApiResponse<>(HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase(), calegResponseDTO.get());
                return ResponseEntity.status(HttpStatus.OK).body(successResponse);
            } else {
                ApiResponse<CalegResponseDTO> notFoundResponse =
                        new ApiResponse<>(HttpStatus.NOT_FOUND.value(), HttpStatus.NOT_FOUND.getReasonPhrase(), null);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(notFoundResponse);
            }
        } catch (Exception e) {
            ApiResponse<CalegResponseDTO> errorResponse =
                    new ApiResponse<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(), null);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }
    }

    @PostMapping("")
    public ResponseEntity<?> saveCaleg(@Valid @RequestBody CalegRequestDTO calegRequestDTO){
        try {
            CalegResponseDTO calegResponseDTO = calegService.save(calegRequestDTO);

            ApiResponse<CalegResponseDTO> successResponse =
                    new ApiResponse<>(HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase(), calegResponseDTO);
            return ResponseEntity.status(HttpStatus.OK).body(successResponse);
        } catch (EntityExistsException e) {
            ApiResponse<CalegResponseDTO> alreadyExistsResponse =
                    new ApiResponse<>(HttpStatus.CONFLICT.value(), e.getMessage(), null);
            return ResponseEntity.status(HttpStatus.CONFLICT).body(alreadyExistsResponse);
        } catch (EntityNotFoundException e) {
            ApiResponse<CalegResponseDTO> notFoundResponse =
                    new ApiResponse<>(HttpStatus.NOT_FOUND.value(), e.getMessage(), null);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(notFoundResponse);
        } catch (Exception e) {
            ApiResponse<CalegResponseDTO> errorResponse =
                    new ApiResponse<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(), null);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateCaleg(@Valid @RequestBody CalegRequestDTO calegRequestDTO, @PathVariable("id") UUID id){
        try {
            CalegResponseDTO calegResponseDTO = calegService.update(calegRequestDTO, id);

            ApiResponse<CalegResponseDTO> successResonse =
                    new ApiResponse<>(HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase(), calegResponseDTO);
            return ResponseEntity.status(HttpStatus.OK).body(successResonse);
        } catch (EntityNotFoundException e) {
            ApiResponse<CalegResponseDTO> notFoundResponse =
                    new ApiResponse<>(HttpStatus.NOT_FOUND.value(), HttpStatus.NOT_FOUND.getReasonPhrase(), null);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(notFoundResponse);
        } catch (EntityExistsException e) {
            ApiResponse<CalegResponseDTO> alreadyExistsResponse =
                    new ApiResponse<>(HttpStatus.CONFLICT.value(), e.getMessage(), null);
            return ResponseEntity.status(HttpStatus.CONFLICT).body(alreadyExistsResponse);
        } catch (Exception e) {
            ApiResponse<CalegResponseDTO> errorResponse =
                    new ApiResponse<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(), null);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteCaleg(@PathVariable("id") UUID id){
        try {
            Optional<CalegResponseDTO>  calegResponseDTO = calegService.findById(id);

            if (calegResponseDTO.isEmpty()){
                ApiResponse<DapilResponseDTO> notFoundResponse =
                        new ApiResponse<>(HttpStatus.NOT_FOUND.value(), HttpStatus.NOT_FOUND.getReasonPhrase(), null);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(notFoundResponse);
            }

            calegService.deleteById(id);
            ApiResponse<CalegResponseDTO> successResponse =
                    new ApiResponse<>(HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase(), calegResponseDTO.get());
            return ResponseEntity.status(HttpStatus.OK).body(successResponse);
        } catch (Exception e) {
            ApiResponse<CalegResponseDTO> errorResponse =
                    new ApiResponse<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(), null);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

}
