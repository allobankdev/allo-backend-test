package com.allobank.allobackendtest.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "wilayah_dapil")
public class WilayahDapilEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "dapil_id", nullable = false)
    private DapilEntity dapilEntity;

    @Column(name = "nama_wilayah", nullable = false)
    private String namaWilayah;
}
