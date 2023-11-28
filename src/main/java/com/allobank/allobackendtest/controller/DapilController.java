package com.allobank.allobackendtest.controller;

import com.allobank.allobackendtest.dto.*;
import com.allobank.allobackendtest.service.DapilService;
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
@RequestMapping("/dapil")
public class DapilController {
  private final DapilService dapilService;

  @PostMapping(
          path = "/create",
          produces = MediaType.APPLICATION_JSON_VALUE,
          consumes = MediaType.APPLICATION_JSON_VALUE
  )
  public ResponseEntity<ResponseData<String>> createDapil(@RequestBody DapilRequestDTO dto){
    dapilService.createDapil(dto);

    return new ResponseEntity<>(
        ResponseData.<String>builder()
                .code(HttpStatus.CREATED.value())
                .status(HttpStatus.CREATED)
                .message("berhasil menambahkan dapil")
                .build(),
        HttpStatus.CREATED);
  }

  @GetMapping(
          path = "/list",
          produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<ResponseData<List<DapilResponseDTO>>> getListDapil(
      @RequestParam(name = "namaDapil", required = false, defaultValue = "") String namaDapil,
      @RequestParam(name = "page", required = false, defaultValue = "0") Integer page,
      @RequestParam(name = "per_page", required = false, defaultValue = "10") Integer limit,
      @RequestParam(name = "sort_by", required = false, defaultValue = "created_at") String sortBy,
      @RequestParam(name = "order_type", required = false, defaultValue = "desc") String sortOrder
  ) {

    var dto = DapilFilterDTO.builder()
            .namaDapil(namaDapil== null ? "": namaDapil)
            .build();

    var pagination = PaginationDTO.builder()
            .page(PaginationUtility.PageForQuery(page))
            .limit(limit)
            .sortBy(sortBy)
            .sortOrder(sortOrder)
            .build();

    var data = dapilService.getListDapil(dto, pagination);
    var total = dapilService.getTotalDapil(dto);

    return new ResponseEntity<>(ResponseData.<List<DapilResponseDTO>>builder()
            .code(HttpStatus.OK.value())
            .status(HttpStatus.OK)
            .message("Berhasil mengambil list dapil")
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
  public ResponseEntity<ResponseData<DapilResponseDTO>> getDetailDapil(
          @PathVariable("id") UUID id
  ) {
    var data = dapilService.getDetailDapilById(id);

    return new ResponseEntity<>(ResponseData.<DapilResponseDTO>builder()
            .code(HttpStatus.OK.value())
            .status(HttpStatus.OK)
            .message("Berhasil mengambil detail dapil")
            .data(data)
            .build(),
            HttpStatus.OK);
  }

  @PutMapping(
          path = "/update/{id}",
          produces = MediaType.APPLICATION_JSON_VALUE,
          consumes = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<ResponseData<DapilResponseDTO>> updateDapil(
          @PathVariable("id") UUID id,
          @RequestBody DapilRequestDTO dto
  ) {

    dapilService.updateDapil(id, dto);

    return new ResponseEntity<>(ResponseData.<DapilResponseDTO>builder()
            .code(HttpStatus.OK.value())
            .status(HttpStatus.OK)
            .message("Success update dapil")
            .build(),
            HttpStatus.OK);
  }

  @PatchMapping(
          path = "/activate/{id}",
          produces = MediaType.APPLICATION_JSON_VALUE
  )
  public ResponseEntity<ResponseData<DapilResponseDTO>> activateDapil(
          @PathVariable("id") UUID dapilId) {

    dapilService.activateDapil(dapilId);

    return new ResponseEntity<>(ResponseData.<DapilResponseDTO>builder()
            .code(HttpStatus.OK.value())
            .status(HttpStatus.OK)
            .message("Berhasil mengaktifkan dapil " + dapilId)
            .build(),
            HttpStatus.OK);
  }

  @PatchMapping(
          path = "/deactivate/{id}",
          produces = MediaType.APPLICATION_JSON_VALUE
  )
  public ResponseEntity<ResponseData<DapilResponseDTO>> deactivateDapil(@PathVariable("id") UUID dapilId) {

    dapilService.deactivateDapil(dapilId);

    return new ResponseEntity<>(ResponseData.<DapilResponseDTO>builder()
            .code(HttpStatus.OK.value())
            .status(HttpStatus.OK)
            .message("Berhasil meng non-aktifkan dapil " + dapilId)
            .build(),
            HttpStatus.OK);
  }
}