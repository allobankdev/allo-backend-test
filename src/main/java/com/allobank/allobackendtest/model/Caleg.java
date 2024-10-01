package com.allobank.allobackendtest.model;

import lombok.Data;

import javax.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "caleg")
@Data
public class Caleg {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "dapil_id")
    private Dapil dapil;

    @ManyToOne
    @JoinColumn(name = "partai_id")
    private Partai partai;

    @Column(name = "nomor_urut")
    private Integer nomorUrut;

    @Column(name = "nama")
    private String nama;

    @Enumerated(EnumType.STRING)
    @Column(name = "jenis_kelamin")
    private JenisKelamin jenisKelamin;
}
