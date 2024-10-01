package com.allobank.allobackendtest.model;

import lombok.Data;

import javax.persistence.*;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "dapil")
@Data
public class Dapil {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(name = "nama_dapil")
    private String namaDapil;

    @Column(name = "provinsi")
    private String provinsi;

    @ElementCollection
    @CollectionTable(name = "wilayah_dapil", joinColumns = @JoinColumn(name = "dapil_id"))
    @Column(name = "wilayah")
    private List<String> wilayahDapilList;

    @Column(name = "jumlah_kursi")
    private int jumlahKursi;
}
