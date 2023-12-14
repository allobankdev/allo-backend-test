package com.allobank.allobackendtest.core.local.partai.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "partai")
public class Partai {
    public Partai(String namaPartai, Integer nomorUrut) {
        this.namaPartai = namaPartai;
        this.nomorUrut = nomorUrut;
    }

    public Partai() {
    }

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(name = "nama_partai")
    private String namaPartai;

    @Column(name = "nomor_urut")
    private Integer nomorUrut;

    public String getId() {
        return this.id;
    }

    public String getNamaPartai() {
        return this.namaPartai;
    }

    public Integer getNomorUrut() {
        return this.nomorUrut;
    }
}
