package com.allobank.allobackendtest.entity;

import com.allobank.allobackendtest.model.JenisKelamin;
import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="caleg")
public class CalegEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "dapil_id", nullable = false)
    private DapilEntity dapil;

    @ManyToOne
    @JoinColumn(name = "partai_id", nullable = false)
    private PartaiEntity partai;

    @Column(name = "nomor_urut")
    private Integer nomorUrut;

    private String nama;

    @Enumerated(EnumType.STRING)
    @Column(name = "jenis_kelamin", length = 20)
    private JenisKelamin jenisKelamin;
}
