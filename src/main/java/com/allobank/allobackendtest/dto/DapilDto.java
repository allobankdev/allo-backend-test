package com.allobank.allobackendtest.dto;

import java.util.UUID;

import javax.validation.constraints.NotBlank;

import lombok.Data;

@Data
public class DapilDto {
  private UUID id;

  @NotBlank(message = "Nama dapil is required")
  private String nama_dapil;
}
