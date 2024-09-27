package com.allobank.allobackendtest.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;


@Setter
@Getter
@Entity
@Table(name = "caleg")
public class Caleg {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "nama_caleg", nullable = false)
    private String namaCaleg;

    @Column(name = "nomor_urut", nullable = false)
    private int nomorUrut;

    @Column(name = "created_at", nullable = false)
    private Date createdAt;

    @ManyToOne
    @JoinColumn(name = "partai_id", nullable = false)
    private Partai partai;

    @ManyToOne
    @JoinColumn(name = "dapil_id", nullable = false)
    private Dapil dapil;

}
