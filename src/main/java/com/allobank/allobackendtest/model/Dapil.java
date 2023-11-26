package com.allobank.allobackendtest.model;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
@Entity
@Table(name = "dapil")
public class Dapil {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(name = "nama_dapil")
    private String namaDapil;

    @Column(name = "provinsi")
    private String provinsi;

    @Column(name = "wilayah_dapil")
    private List<String> wilayahDapilList;

    @Column(name = "jumlah_kursi")
    private int jumlahKursi;
}
