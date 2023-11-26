package com.allobank.allobackendtest.model;

import jakarta.persistence.*;
import lombok.Data;

import java.util.UUID;


@Data
@Entity
@Table(name = "caleg")
public class Caleg {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "dapil_id", referencedColumnName = "id")
    private Dapil dapil;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "partai_id", referencedColumnName = "id")
    private Partai partai;

    @Column(name = "nomor_urut")
    private Integer nomorUrut;

    @Column(name = "nama")
    private String nama;

    @Column(name = "jenis_kelamin")
    private JenisKelamin jenisKelamin;
}
