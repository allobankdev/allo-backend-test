package com.allobank.allobackendtest.model;

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
public class Dapil {

    @Id
    private UUID id;

    @Column(name = "nama_dapil", nullable = false)
    private String namaDapil;

    @Column(name = "provinsi", nullable = false)
    private String provinsi;

    @Column(name = "jumlah_kursi", nullable = false)
    private int jumlahKursi;

    @OneToMany(mappedBy = "dapil", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<WilayahDapil> wilayahDapilList;

}
