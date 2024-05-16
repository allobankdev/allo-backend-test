package com.allobank.allobackendtest.model;

import jakarta.persistence.*;
import lombok.Data;

import java.io.Serializable;
import java.util.UUID;

@Data
@Entity
@Table(name = "caleg")
public class Caleg extends AbstractDate implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "dapil_id")
    private Dapil dapil;

    @ManyToOne
    @JoinColumn(name = "partai_id")
    private Partai partai;

    private Integer nomorUrut;

    private String nama;

    @Enumerated(EnumType.STRING)
    private JenisKelamin jenisKelamin;
}
