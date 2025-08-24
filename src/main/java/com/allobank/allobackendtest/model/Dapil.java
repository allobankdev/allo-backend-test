package com.allobank.allobackendtest.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Data
@Entity
@Table(name = "electoral_district")
@AllArgsConstructor
@NoArgsConstructor
public class Dapil {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(name = "name", length = 50, nullable = false)
    private String namaDapil;

    @Column(name = "province", length = 50, nullable = false)
    private String provinsi;

    @Column(name = "region", nullable = false)
    private List<String> wilayahDapilList;

    @Column(name = "seat_count", nullable = false)
    private int jumlahKursi;
}
