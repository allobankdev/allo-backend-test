package com.allobank.allobackendtest.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.util.List;

@Data
public class DapilRequestDTO {
    @NotBlank(message = "Electoral District name is required")
    private String namaDapil;
    @NotBlank(message = "Province name is required")
    private String provinsi;
    @NotBlank(message = "Region is required")
    private List<String> wilayahDapilList;
    @NotBlank(message = "Total seat is required")
    private int jumlahKursi;

}
