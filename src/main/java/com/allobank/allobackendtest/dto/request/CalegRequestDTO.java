package com.allobank.allobackendtest.dto.request;

import com.allobank.allobackendtest.model.JenisKelamin;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CalegRequestDTO {
    @NotBlank(message = "Legislative name is required")
    private String nama;
    @NotBlank(message = "Order number is required")
    private Integer nomorUrut;
    @NotBlank(message = "Gender is required")
    private JenisKelamin jenisKelamin;
    @NotBlank(message = "Party is required")
    private String namaPartai;
    @NotBlank(message = "Electoral District is required")
    private String namaDapil;
}
