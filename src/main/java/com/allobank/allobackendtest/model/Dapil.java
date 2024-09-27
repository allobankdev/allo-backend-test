package com.allobank.allobackendtest.model;

import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
public class Dapil {
    private UUID id;
    private String namaDapil;
    private String provinsi;
    private List<String> wilayahDapilList;
    private int jumlahKursi;
}
