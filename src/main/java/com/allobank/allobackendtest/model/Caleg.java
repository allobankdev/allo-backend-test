package com.allobank.allobackendtest.model;
import jakarta.persistence.*;
import lombok.Data;
import java.util.UUID;

@Entity
@Data
@Table(name = "caleg")
public class Caleg {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private String id;
    @ManyToOne
    @JoinColumn(name = "dapil_id", nullable = false)
    private Dapil dapil;
    @ManyToOne
    @JoinColumn(name = "partai_id", nullable = false)
    private Partai partai;
    private Integer nomorUrut;
    @Column(name="nama", nullable = false)
    private String nama;

    @Enumerated(EnumType.STRING)
    @Column(name = "JenisKelamin")
    private JenisKelamin jenisKelamin;

    public Caleg() {
        // Default constructor
    }
    public Caleg(String id, Dapil dapil, Partai partai, Integer nomorUrut, String nama, JenisKelamin jenisKelamin) {
        this.id = id;
        this.dapil = dapil;
        this.partai = partai;
        this.nomorUrut = nomorUrut;
        this.nama = nama;
        this.jenisKelamin = jenisKelamin;
    }
}
