package com.allobank.allobackendtest.controller;

import com.allobank.allobackendtest.dto.*;
import com.allobank.allobackendtest.service.PartaiService;
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
@RequestMapping("/partai")
public class PartaiController {
  private final PartaiService partaiService;

  @PostMapping(
          path = "/create",
          produces = MediaType.APPLICATION_JSON_VALUE,
          consumes = MediaType.APPLICATION_JSON_VALUE
  )
  public ResponseEntity<ResponseData<String>> createPartai(@RequestBody PartaiRequestDTO dto){
    partaiService.createPartai(dto);

    return new ResponseEntity<>(
        ResponseData.<String>builder()
                .code(HttpStatus.CREATED.value())
                .status(HttpStatus.CREATED)
                .message("berhasil menambahkan partai")
                .build(),
        HttpStatus.CREATED);
  }

  @GetMapping(
          path = "/list",
          produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<ResponseData<List<PartaiResponseDTO>>> getListPartai(
      @RequestParam(name = "namaPartai", required = false, defaultValue = "") String namaPartai,
      @RequestParam(name = "nomorUrut", required = false, defaultValue = "0") Integer nomorUrut,
      @RequestParam(name = "page", required = false, defaultValue = "0") Integer page,
      @RequestParam(name = "per_page", required = false, defaultValue = "10") Integer limit,
      @RequestParam(name = "sort_by", required = false, defaultValue = "created_at") String sortBy,
      @RequestParam(name = "order_type", required = false, defaultValue = "desc") String sortOrder
  ) {

    var dto = PartaiFilterDTO.builder()
            .namaPartai(namaPartai== null ? "": namaPartai)
            .nomorUrut(nomorUrut == null ? 0: nomorUrut)
            .build();

    var pagination = PaginationDTO.builder()
            .page(PaginationUtility.PageForQuery(page))
            .limit(limit)
            .sortBy(sortBy)
            .sortOrder(sortOrder)
            .build();

    var data = partaiService.getListPartai(dto, pagination);
    var total = partaiService.getTotalPartai(dto);

    return new ResponseEntity<>(ResponseData.<List<PartaiResponseDTO>>builder()
            .code(HttpStatus.OK.value())
            .status(HttpStatus.OK)
            .message("Berhasil mengambil list partai")
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
  public ResponseEntity<ResponseData<PartaiResponseDTO>> getDetailPartai(
          @PathVariable("id") UUID id
  ) {
    var data = partaiService.getDetailPartaiById(id);

    return new ResponseEntity<>(ResponseData.<PartaiResponseDTO>builder()
            .code(HttpStatus.OK.value())
            .status(HttpStatus.OK)
            .message("Berhasil mengambil detail partai")
            .data(data)
            .build(),
            HttpStatus.OK);
  }

  @PutMapping(
          path = "/update/{id}",
          produces = MediaType.APPLICATION_JSON_VALUE,
          consumes = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<ResponseData<PartaiResponseDTO>> updatePartai(
          @PathVariable("id") UUID id,
          @RequestBody PartaiRequestDTO dto
  ) {

    partaiService.updatePartai(id, dto);

    return new ResponseEntity<>(ResponseData.<PartaiResponseDTO>builder()
            .code(HttpStatus.OK.value())
            .status(HttpStatus.OK)
            .message("Success update partai")
            .build(),
            HttpStatus.OK);
  }

  @PatchMapping(
          path = "/activate/{id}",
          produces = MediaType.APPLICATION_JSON_VALUE
  )
  public ResponseEntity<ResponseData<PartaiResponseDTO>> activatePartai(
          @PathVariable("id") UUID partaiId) {

    partaiService.activatePartai(partaiId);

    return new ResponseEntity<>(ResponseData.<PartaiResponseDTO>builder()
            .code(HttpStatus.OK.value())
            .status(HttpStatus.OK)
            .message("Berhasil mengaktifkan partai " + partaiId)
            .build(),
            HttpStatus.OK);
  }

  @PatchMapping(
          path = "/deactivate/{id}",
          produces = MediaType.APPLICATION_JSON_VALUE
  )
  public ResponseEntity<ResponseData<PartaiResponseDTO>> deactivatePartai(@PathVariable("id") UUID partaiId) {

    partaiService.deactivatePartai(partaiId);

    return new ResponseEntity<>(ResponseData.<PartaiResponseDTO>builder()
            .code(HttpStatus.OK.value())
            .status(HttpStatus.OK)
            .message("Berhasil meng non-aktifkan partai " + partaiId)
            .build(),
            HttpStatus.OK);
  }
}