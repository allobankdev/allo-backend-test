package com.allobank.allobackendtest.dto;

import com.allobank.allobackendtest.model.JenisKelamin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.UUID;

@Data
public class CalegDto {

    @NotNull(message = "Dapil wajib diisi")
    private UUID dapil;

    @NotNull(message = "Partai wajib diisi")
    private UUID partai;

    @NotNull(message = "Nomor urut wajib diisi")
    @Min(value = 1, message = "Nomor urut minimal 1")
    private Integer nomorUrut;

    @NotBlank(message = "Nama caleg tidak boleh kosong")
    private String nama;

    @NotNull(message = "Jenis kelamin wajib diisi")
    private JenisKelamin jenisKelamin;
}
