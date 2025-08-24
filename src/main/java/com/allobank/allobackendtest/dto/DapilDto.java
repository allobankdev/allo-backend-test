package com.allobank.allobackendtest.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.List;

@Data
public class DapilDto {

    @NotBlank(message = "Nama dapil tidak boleh kosong")
    private String namaDapil;

    @NotBlank(message = "Provinsi tidak boleh kosong")
    private String provinsi;

    @NotNull(message = "Wilayah dapil wajib diisi")
    @Size(min = 1, message = "Wilayah dapil minimal 1")
    private List<String> wilayahDapilList;

    @NotNull(message = "jumlah Kusi tidak boleh kosong")
    @Min(value = 1, message = "Jumlah kursi minimal 1")
    private Integer jumlahKursi;
}
