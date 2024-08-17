package com.allobank.allobackendtest.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "caleg")
public class Caleg {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name="nama", nullable = false)
    private String nama;

    @Column(name="nomor_urut", nullable = false)
    private Integer nomorUrut;

    @Column(name="jenis_kelamin", nullable = false)
    private JenisKelamin jenisKelamin;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "dapil_id", nullable = false)
    private Dapil dapil;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "partai_id", nullable = false)
    private Partai partai;
}