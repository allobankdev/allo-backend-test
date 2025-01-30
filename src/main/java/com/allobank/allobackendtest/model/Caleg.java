package com.allobank.allobackendtest.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.Data;

import java.util.UUID;
@Entity
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
    private JenisKelamin jenisKelamin;
}


