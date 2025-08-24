package com.allobank.allobackendtest.dto.response;

import lombok.Data;

import java.util.List;

@Data
public class DapilResponseDTO {
    private String namaDapil;
    private String provinsi;
    private List<String> wilayahDapilList;
    private int jumlahKursi;
}
