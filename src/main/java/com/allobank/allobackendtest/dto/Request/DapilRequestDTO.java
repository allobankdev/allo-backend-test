package com.allobank.allobackendtest.dto.Request;

import lombok.Data;

import java.util.List;

@Data
public class DapilRequestDTO {
    private String namaDapil;
    private String provinsi;
    private List<String> wilayahDapilList;
    private int jumlahKursi;
}
