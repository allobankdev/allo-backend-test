package com.allobank.allobackendtest.dto;

import lombok.Builder;
import lombok.Data;

import java.sql.Timestamp;
import java.util.UUID;

@Data
@Builder
public class PartaiFilterDTO {
  private String namaPartai;
  private Integer nomorUrut;
}
