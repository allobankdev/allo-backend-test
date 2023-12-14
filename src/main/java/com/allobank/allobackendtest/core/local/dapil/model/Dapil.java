package com.allobank.allobackendtest.core.local.dapil.model;import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "dapil")
public class Dapil {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    public Dapil() {}

    public Dapil(String namaDapil, String provinsi, int jumlahKursi) {
        this.namaDapil = namaDapil;
        this.provinsi = provinsi;
        this.jumlahKursi = jumlahKursi;
    }

    @Column(name = "nama_dapil")
    private String namaDapil;

    @Column(name = "provinsi")
    private String provinsi;

    @Column(name = "jumlah_kursi")
    private int jumlahKursi;

    public String getId() {
        return this.id;
    }
}
