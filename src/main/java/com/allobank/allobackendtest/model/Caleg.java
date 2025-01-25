package com.allobank.allobackendtest.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import lombok.Data;

import java.util.UUID;

@Data
@Entity
@Table(name = "caleg", schema = "public")
public class Caleg {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id")
    private UUID id;
    @ManyToOne
    @JoinColumn(name = "dapil_id", referencedColumnName = "id")
    private Dapil dapil;
    @ManyToOne
    @JoinColumn(name = "partai_id", referencedColumnName = "id")
    private Partai partai;
    @Column(name = "nomor_urut")
    private Integer nomorUrut;
    @Column(name = "nama")
    private String nama;
    @Column(name = "jenis_kelamin")
    private JenisKelamin jenisKelamin;
}
