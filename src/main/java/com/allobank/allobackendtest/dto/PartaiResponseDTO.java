package com.allobank.allobackendtest.dto;

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
public class PartaiResponseDTO {
  private UUID id;
  private String namaPartai;
  private Integer nomorUrut;
  private Timestamp createdAt;
  private Timestamp updatedAt;
  private Timestamp deletedAt;
}
