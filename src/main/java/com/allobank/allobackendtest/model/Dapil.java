package com.allobank.allobackendtest.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
@Entity
@Table(name = "dapil")
public class Dapil {
    @Id
    private UUID id;
    @Column(name = "nama_dapil")
    private String namaDapil;
    private String provinsi;
    @Column(name = "wilayah_dapil_list")
    private List<String> wilayahDapilList;
    @Column(name = "jumlah_kursi")
    private int jumlahKursi;
}
