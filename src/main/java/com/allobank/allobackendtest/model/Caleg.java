package com.allobank.allobackendtest.model;

import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class Caleg {
    private UUID id;
    private Dapil dapil;
    private Partai partai;
    private Integer nomorUrut;
    private String nama;
    private JenisKelamin jenisKelamin;
}