package com.allobank.allobackendtest.dto;

import lombok.Builder;
import lombok.Data;

import java.sql.Timestamp;
import java.util.List;
import java.util.UUID;

@Data
@Builder
public class DapilFilterDTO {
  private String namaDapil;
  private String provinsi;
  private Integer jumlahKursi;
}
