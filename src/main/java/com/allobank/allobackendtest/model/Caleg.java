package com.allobank.allobackendtest.model;

import jakarta.persistence.*;
import lombok.Data;

import java.util.UUID;

@Entity
@Table(name = "caleg")
@Data
public class Caleg {
    @Id
    private UUID id;
    @ManyToOne
    private Dapil dapil;
    @ManyToOne
    private Partai partai;
    private Integer nomorUrut;
    private String nama;
    @Enumerated(EnumType.STRING)
    private JenisKelamin jenisKelamin;
}
