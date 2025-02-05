package com.allobank.allobackendtest.entity;

import com.allobank.allobackendtest.model.JenisKelamin;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "caleg")
public class CalegEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "dapil_id", nullable = false)
    private DapilEntity dapilEntity;

    @ManyToOne
    @JoinColumn(name = "partai_id", nullable = false)
    private PartaiEntity partaiEntity;

    @Column(name = "nomor_urut", nullable = false)
    private Integer nomorUrut;

    @Column(name = "nama", nullable = false)
    private String nama;

    @Enumerated(EnumType.STRING)
    @Column(name = "jenis_kelamin", nullable = false)
    private JenisKelamin jenisKelamin;
}
