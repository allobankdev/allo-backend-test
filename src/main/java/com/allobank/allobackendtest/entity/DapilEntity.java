package com.allobank.allobackendtest.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "dapil")
public class DapilEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "nama_dapil", nullable = false, unique = true)
    private String namaDapil;

    @Column(name = "provinsi", nullable = false)
    private String provinsi;

    @Column(name = "jumlah_kursi", nullable = false)
    private int jumlahKursi;

    @OneToMany(mappedBy = "dapilEntity")
    private List<WilayahDapilEntity> wilayahDapilEntityList;
}
