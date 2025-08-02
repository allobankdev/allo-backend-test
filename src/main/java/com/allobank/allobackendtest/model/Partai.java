package com.allobank.allobackendtest.model;

import lombok.Data;
import jakarta.persistence.*;
import java.util.UUID;

@Data
@Entity
@Table(name = "partai", schema = "pemilu")
public class Partai {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "nama_partai", nullable = false)
    private String namaPartai;

    @Column(name = "nomor_urut", nullable = false, unique = true)
    private Integer nomorUrut;
}