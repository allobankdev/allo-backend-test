package com.allobank.allobackendtest.dto;

import java.util.UUID;

import javax.validation.constraints.NotBlank;

import lombok.Data;

@Data
public class PartaiDto {
  private UUID id;

  @NotBlank(message = "Nama partai is required")
  private String nama_partai;
}
