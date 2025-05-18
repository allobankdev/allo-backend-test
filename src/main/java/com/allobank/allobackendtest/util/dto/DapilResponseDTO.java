package com.allobank.allobackendtest.util.dto;

import com.allobank.allobackendtest.model.Dapil;
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

    public DapilResponseDTO(Dapil dapil) {
        this.id = dapil.getId();
        this.namaDapil = dapil.getNamaDapil();
        this.provinsi = dapil.getProvinsi();
        this.wilayahDapilList = dapil.getWilayahDapilList();
        this.jumlahKursi = dapil.getJumlahKursi();
    }
}
