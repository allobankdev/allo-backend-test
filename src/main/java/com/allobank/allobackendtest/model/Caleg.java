package com.allobank.allobackendtest.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Entity(name = "Caleg")
public class Caleg {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "caleg_id")
    private UUID id;
    @OneToOne
    @JoinColumn(name = "dapil_id")
    @Fetch(FetchMode.JOIN)
    private Dapil dapil;
    @OneToOne
    @JoinColumn(name = "partai_id")
    @Fetch(FetchMode.JOIN)
    private Partai partai;
    private Integer nomorUrut;
    private String nama;
    private JenisKelamin jenisKelamin;
}
