package com.allobank.allobackendtest.model;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

import jakarta.persistence.Entity;

@Data
@Entity
@Setter
@Getter
public class Caleg {
    private UUID id;
    private Dapil dapil;
    private Partai partai;
    private Integer nomorUrut;
    private String nama;
    private JenisKelamin jenisKelamin;
}
