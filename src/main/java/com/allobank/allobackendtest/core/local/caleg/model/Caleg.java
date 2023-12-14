package com.allobank.allobackendtest.core.local.caleg.model;

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
    private String id;

    @Column(name = "dapil_id")
    private String dapil_id;

    @Column(name = "partai_id")
    private String partai_id;

    @Column(name = "nomor_urut")
    private Integer nomorUrut;

    @Column(name = "nama")
    private String nama;

    @Column(name = "jenis_kelamin")
    private JenisKelamin jenisKelamin;

    public Caleg() {}

    public Caleg(String dapil_id, String partai_id, Integer nomorUrut, String nama, JenisKelamin jenisKelamin) {
        this.dapil_id = dapil_id;
        this.partai_id = partai_id;
        this.nomorUrut = nomorUrut;
        this.nama = nama;
        this.jenisKelamin = jenisKelamin;
    }

    public String getId() {
        return id;
    }

    public String getNama() {
        return nama;
    }

    public String getDapilId() {
        return dapil_id;
    }

    public String getPartaiId() {
        return partai_id;
    }

    public int getNomorUrut() {
        return nomorUrut;
    }

    public String getJenisKelamin() {
        if (jenisKelamin == JenisKelamin.LAKILAKI) {
            return "Laki Laki";
        } else {
            return "Perempuan";
        }
    }
}
