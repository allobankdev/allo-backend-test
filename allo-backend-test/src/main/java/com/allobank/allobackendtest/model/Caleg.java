package com.allobank.allobackendtest.model;

import com.allobank.allobackendtest.model.enums.JenisKelamin;

import java.util.UUID;

public class Caleg {

    private UUID id;
    private String nama;
    private Dapil dapil;
    private Partai partai;
    private Integer nomorUrut;
    private JenisKelamin jenisKelamin;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public Dapil getDapil() {
        return dapil;
    }

    public void setDapil(Dapil dapil) {
        this.dapil = dapil;
    }

    public Partai getPartai() {
        return partai;
    }

    public void setPartai(Partai partai) {
        this.partai = partai;
    }

    public Integer getNomorUrut() {
        return nomorUrut;
    }

    public void setNomorUrut(Integer nomorUrut) {
        this.nomorUrut = nomorUrut;
    }

    public JenisKelamin getJenisKelamin() {
        return jenisKelamin;
    }

    public void setJenisKelamin(JenisKelamin jenisKelamin) {
        this.jenisKelamin = jenisKelamin;
    }
}