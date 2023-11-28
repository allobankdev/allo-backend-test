package com.allobank.allobackendtest.controller;

import com.allobank.allobackendtest.dto.*;
import com.allobank.allobackendtest.service.CalegService;
import com.allobank.allobackendtest.utility.PaginationUtility;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping("/caleg")
public class CalegController {
  private final CalegService calegService;

  @PostMapping(
          path = "/create",
          produces = MediaType.APPLICATION_JSON_VALUE,
          consumes = MediaType.APPLICATION_JSON_VALUE
  )
  public ResponseEntity<ResponseData<String>> createCaleg(@RequestBody CalegRequestDTO dto){
    calegService.createCaleg(dto);

    return new ResponseEntity<>(
        ResponseData.<String>builder()
                .code(HttpStatus.CREATED.value())
                .status(HttpStatus.CREATED)
                .message("berhasil menambahkan caleg")
                .build(),
        HttpStatus.CREATED);
  }

  @GetMapping(
          path = "/list",
          produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<ResponseData<List<CalegResponseDTO>>> getListCaleg(
      @RequestParam(name = "namaCaleg", required = false, defaultValue = "") String namaCaleg,
      @RequestParam(name = "nomorUrut", required = false, defaultValue = "0") Integer nomorUrut,
      @RequestParam(name = "page", required = false, defaultValue = "0") Integer page,
      @RequestParam(name = "per_page", required = false, defaultValue = "10") Integer limit,
      @RequestParam(name = "sort_by", required = false, defaultValue = "nomor_urut") String sortBy,
      @RequestParam(name = "order_type", required = false, defaultValue = "desc") String sortOrder
  ) {

    var dto = CalegFilterDTO.builder()
            .nama(namaCaleg== null ? "": namaCaleg)
            .nomorUrut(nomorUrut == null ? 0: nomorUrut)
            .build();

    var pagination = PaginationDTO.builder()
            .page(PaginationUtility.PageForQuery(page))
            .limit(limit)
            .sortBy(sortBy)
            .sortOrder(sortOrder)
            .build();

    var data = calegService.getListCaleg(dto, pagination);
    var total = calegService.getTotalCaleg(dto);

    return new ResponseEntity<>(ResponseData.<List<CalegResponseDTO>>builder()
            .code(HttpStatus.OK.value())
            .status(HttpStatus.OK)
            .message("Berhasil mengambil list caleg")
            .metadata(
              ResponseMetadata.builder()
              .page(PaginationUtility.PageForView(page))
              .totalPage(PaginationUtility.TotalPages(total, limit))
              .perPage(limit)
              .total(total)
              .sortBy(sortBy)
              .orderType(sortOrder)
              .build()
            )
            .data(data)
            .build(),
            HttpStatus.OK);
  }

  @GetMapping(
    path = "/detail/{id}",
    produces = MediaType.APPLICATION_JSON_VALUE
  )
  public ResponseEntity<ResponseData<CalegResponseDTO>> getDetailCaleg(
          @PathVariable("id") UUID id
  ) {
    var data = calegService.getDetailCalegById(id);

    return new ResponseEntity<>(ResponseData.<CalegResponseDTO>builder()
            .code(HttpStatus.OK.value())
            .status(HttpStatus.OK)
            .message("Berhasil mengambil detail caleg")
            .data(data)
            .build(),
            HttpStatus.OK);
  }

  @PutMapping(
          path = "/update/{id}",
          produces = MediaType.APPLICATION_JSON_VALUE,
          consumes = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<ResponseData<CalegResponseDTO>> updateCaleg(
          @PathVariable("id") UUID id,
          @RequestBody CalegRequestDTO dto
  ) {

    calegService.updateCaleg(id, dto);

    return new ResponseEntity<>(ResponseData.<CalegResponseDTO>builder()
            .code(HttpStatus.OK.value())
            .status(HttpStatus.OK)
            .message("Success update caleg")
            .build(),
            HttpStatus.OK);
  }

  @PatchMapping(
          path = "/activate/{id}",
          produces = MediaType.APPLICATION_JSON_VALUE
  )
  public ResponseEntity<ResponseData<CalegResponseDTO>> activateCaleg(
          @PathVariable("id") UUID calegId) {

    calegService.activateCaleg(calegId);

    return new ResponseEntity<>(ResponseData.<CalegResponseDTO>builder()
            .code(HttpStatus.OK.value())
            .status(HttpStatus.OK)
            .message("Berhasil mengaktifkan caleg " + calegId)
            .build(),
            HttpStatus.OK);
  }

  @PatchMapping(
          path = "/deactivate/{id}",
          produces = MediaType.APPLICATION_JSON_VALUE
  )
  public ResponseEntity<ResponseData<CalegResponseDTO>> deactivateCaleg(@PathVariable("id") UUID calegId) {

    calegService.deactivateCaleg(calegId);

    return new ResponseEntity<>(ResponseData.<CalegResponseDTO>builder()
            .code(HttpStatus.OK.value())
            .status(HttpStatus.OK)
            .message("Berhasil meng non-aktifkan caleg " + calegId)
            .build(),
            HttpStatus.OK);
  }
}