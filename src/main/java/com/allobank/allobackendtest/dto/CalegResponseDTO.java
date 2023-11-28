package com.allobank.allobackendtest.dto;

import com.allobank.allobackendtest.model.Dapil;
import com.allobank.allobackendtest.model.JenisKelamin;
import com.allobank.allobackendtest.model.Partai;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.sql.Timestamp;
import java.util.UUID;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@Data
@Builder
@AllArgsConstructor
public class CalegResponseDTO {
  private UUID id;
  private Dapil dapil;
  private Partai partai;
  private Integer nomorUrut;
  private String nama;
  private JenisKelamin jenisKelamin;
  private Timestamp createdAt;
  private Timestamp updatedAt;
  private Timestamp deletedAt;
}
