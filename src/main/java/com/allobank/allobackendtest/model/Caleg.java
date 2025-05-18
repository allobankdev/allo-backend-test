package com.allobank.allobackendtest.model;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Data;

import java.util.UUID;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Data
@Entity
@Table(name = "caleg")
public class Caleg{

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;   

    @ManyToOne
    @JoinColumn(name = "dapil_id", nullable = false)
    private Dapil dapil;

    @ManyToOne
    @JoinColumn(name = "partai_id", nullable = false)
    private Partai partai;
    private Integer nomorUrut;
    private String nama;
    private JenisKelamin jenisKelamin;
}
