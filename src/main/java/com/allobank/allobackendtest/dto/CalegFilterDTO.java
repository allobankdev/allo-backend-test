package com.allobank.allobackendtest.dto;

import com.allobank.allobackendtest.model.JenisKelamin;
import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class CalegFilterDTO {
  private UUID dapilId;
  private UUID partaiId;
  private String namaDapil;
  private String namaPartai;
  private Integer nomorUrut;
  private String nama;
  private JenisKelamin jenisKelamin;
}
