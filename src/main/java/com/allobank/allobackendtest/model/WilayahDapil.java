package com.allobank.allobackendtest.model;

import lombok.Data;

import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Data
@Entity
@Table(name = "wilayah_dapil", schema = "public")
public class WilayahDapil {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id")
    private UUID id;
    @Column(name = "nama_wilayah")
    private String nama_wilayah;
    @ManyToOne
    @JoinColumn(name = "dapil_id", referencedColumnName = "id")
    private Dapil dapil;
}
