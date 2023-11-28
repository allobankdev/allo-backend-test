package com.allobank.allobackendtest.dto;

import com.allobank.allobackendtest.model.JenisKelamin;
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
public class CalegRequestDTO {
    @JsonIgnore
    private UUID id;

    @NotNull(message = "dapil id harus dipilih")
    private UUID dapilId;

    @NotNull(message = "partai id harus dipilih")
    private UUID partaiId;

    @NotNull(message = "nomor harus diisi")
    private Integer nomorUrut;

    @NotBlank(message = "nama harus diisi")
    private String nama;

    @NotNull(message = "jenis kelamin harus diisi")
    private JenisKelamin jenisKelamin;
}

