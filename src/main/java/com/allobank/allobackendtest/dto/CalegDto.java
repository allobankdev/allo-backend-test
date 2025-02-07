package com.allobank.allobackendtest.dto;

import java.util.UUID;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import lombok.Data;

@Data
public class CalegDto {
  private UUID id;

  @NotBlank(message = "Nama is required")
  private String nama;

  @NotNull(message = "Nomor urut is required")
  private Integer nomor_urut;

  @Pattern(regexp = "LAKI-LAKI|PEREMPUAN", message = "Jenis kelamin harus LAKI-LAKI atau PEREMPUAN")
  private String jenisKelamin;

  private String alamat;

  @NotNull(message = "Dapil is required")
  private UUID dapilId;

  @NotNull(message = "Partai is required")
  private UUID partaiId;
}
