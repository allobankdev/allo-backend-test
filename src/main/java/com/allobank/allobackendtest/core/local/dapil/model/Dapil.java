package com.allobank.allobackendtest.core.local.dapil.model;

import java.util.List;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "dapil")
public class Dapil {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "nama_dapil")
    private String namaDapil;

    @Column(name = "provinsi")
    private String provinsi;

    @Column(name = "wilayahDapilList")
    private List<String> wilayahDapilList;

    @Column(name = "jumlah_kursi")
    private int jumlahKursi;
}
