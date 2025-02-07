package com.allobank.allobackendtest.dto;

import java.util.UUID;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import lombok.Data;

@Data
public class CalegDto {
  private String id; // Ubah ke String
  @NotBlank(message = "Nama is required")
  private String nama;
  @NotNull(message = "Nomor urut is required")
  private Integer nomor_urut;
  @Pattern(regexp = "LAKILAKI|PEREMPUAN", message = "Jenis kelamin harus LAKILAKI atau PEREMPUAN")
  private String jenisKelamin;
  private String alamat;
  @NotNull(message = "Dapil is required")
  private String dapilId; // Ubah ke String
  @NotNull(message = "Partai is required")
  private String partaiId; // Ubah ke String
}
