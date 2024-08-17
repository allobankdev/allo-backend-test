package com.allobank.allobackendtest.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "dapil")
public class Dapil {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name="nama_dapil", nullable = false)
    private String namaDapil;

    @Column(name="provinsi", nullable = false)
    private String provinsi;

    @Column(name="wilayah_dapil", nullable = false)
    private List<String> wilayahDapilList;

    @Column(name="jumlah_kursi", nullable = false)
    private int jumlahKursi;
}
