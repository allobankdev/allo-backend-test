package com.allobank.allobackendtest.dto.Response;

import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
public class DapilResponseDTO {
    private UUID id;
    private String namaDapil;
    private String provinsi;
    private List<String> wilayahDapilList;
    private int jumlahKursi;
}