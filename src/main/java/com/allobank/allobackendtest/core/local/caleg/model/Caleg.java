package com.allobank.allobackendtest.core.local.caleg.model;

import java.util.UUID;

import com.allobank.allobackendtest.model.JenisKelamin;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "caleg")
public class Caleg {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "dapil_id")
    private UUID dapil_id;

    @Column(name = "partai_id")
    private UUID partai_id;

    @Column(name = "nomor_urut")
    private Integer nomorUrut;

    @Column(name = "nama")
    private String nama;

    @Column(name = "jenis_kelamin")
    private JenisKelamin jenisKelamin;

    public String getNama() {
        return nama;
    }
}
