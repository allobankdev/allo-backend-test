package com.allobank.allobackendtest.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@Data
public class DapilRequestDTO {
  @JsonIgnore
  private UUID id;

  @NotBlank(message = "nama dapil harus diisi")
  private String namaDapil;

  @NotBlank(message = "provinsi harus diisi")
  private String provinsi;

  @NotNull(message = "wilayah dapil harus diisi")
  private List<String> wilayahDapilList;

  @NotNull(message = "jumlah kursi harus diisi")
  private Integer jumlahKursi;
}
