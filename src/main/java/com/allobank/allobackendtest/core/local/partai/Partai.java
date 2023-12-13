package com.allobank.allobackendtest.core.local.partai;

import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "partai")
public class Partai {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "nama_partai")
    private String namaPartai;

    @Column(name = "nomor_urut")
    private Integer nomorUrut;
}
