package com.allobank.allobackendtest.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PartaiDto {

    @NotBlank(message = "Nama partai tidak boleh kosong")
    private String namaPartai;

    @NotNull(message = "Nomor urut wajib diisi")
    @Min(value = 1, message = "Nomor urut minimal 1")
    private Integer nomorUrut;
}
