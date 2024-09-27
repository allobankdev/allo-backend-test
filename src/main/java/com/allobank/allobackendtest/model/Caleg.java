package com.allobank.allobackendtest.model;

import lombok.Data;

import java.util.UUID;

@Data
public class Caleg {
    private UUID id;
    private Dapil dapil;
    private Partai partai;
    private Integer nomorUrut;
    private String nama;
    private JenisKelamin jenisKelamin;
}
