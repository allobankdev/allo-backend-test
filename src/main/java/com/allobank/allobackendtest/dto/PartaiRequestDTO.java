package com.allobank.allobackendtest.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.UUID;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@Data
public class PartaiRequestDTO {
  @NotBlank(message = "nama partai harus diisi")
  private String namaPartai;

  @NotNull(message = "nomor urut harus diisi")
  private Integer nomorUrut;
}
