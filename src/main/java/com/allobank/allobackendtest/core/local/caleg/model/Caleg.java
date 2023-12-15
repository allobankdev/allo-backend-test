package com.allobank.allobackendtest.core.local.caleg.model;

import com.allobank.allobackendtest.core.local.dapil.model.Dapil;
import com.allobank.allobackendtest.core.local.partai.model.Partai;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "caleg")
public class Caleg {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @ManyToOne
    @JoinColumn(name = "dapil_id", nullable = false)
    private Dapil dapil;

    @ManyToOne
    @JoinColumn(name = "partai_id", nullable = false)
    private Partai partai;

    @Column(name = "nomor_urut")
    private Integer nomorUrut;

    @Column(name = "nama")
    private String nama;

    @Column(name = "jenis_kelamin")
    private JenisKelamin jenisKelamin;

    public Caleg() {
    }

    public Caleg(Dapil dapil_id, Partai partai_id, Integer nomorUrut, String nama, JenisKelamin jenisKelamin) {
        this.dapil = dapil_id;
        this.partai = partai_id;
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

    public Dapil getDapilId() {
        return dapil;
    }

    public Partai getPartaiId() {
        return partai;
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
