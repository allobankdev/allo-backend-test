// Caleg.java
package com.allobank.allobackendtest.model;

import lombok.Data;
import jakarta.persistence.*;
import java.util.UUID;

@Data
@Entity
@Table(name = "caleg", schema = "pemilu")
public class Caleg {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "dapil_id", nullable = false)
    private Dapil dapil;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "partai_id", nullable = false)
    private Partai partai;

    @Column(name = "nomor_urut", nullable = false)
    private Integer nomorUrut;

    @Column(nullable = false)
    private String nama;

    @Enumerated(EnumType.STRING)
    @Column(name = "jenis_kelamin", nullable = false)
    private JenisKelamin jenisKelamin;
}