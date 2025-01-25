package com.allobank.allobackendtest.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
@Entity
@Table(name = "dapil", schema = "public")
public class Dapil {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id")
    private UUID id;
    @Column(name = "nama_dapil")
    private String namaDapil;
    @Column(name = "provinsi")
    private String provinsi;
    @OneToMany(mappedBy = "dapil", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<WilayahDapil> wilayahDapilList;
    @Column(name = "jumlah_kursi")
    private int jumlahKursi;
}
