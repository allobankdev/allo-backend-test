package com.allobank.allobackendtest.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import java.util.UUID;

@Data
@Entity
@Table(name = "caleg")
public class CalegEntity {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "id", updatable = false, nullable = false, columnDefinition = "uuid")
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "dapil_id")
    private DapilEntity dapil;

    @ManyToOne
    @JoinColumn(name = "partai_id")
    private PartaiEntity partai;

    @Column(name = "nomor_urut")
    private Integer nomorUrut;

    private String nama;

    @Enumerated(EnumType.STRING)
    @Column(name = "jenis_kelamin")
    private JenisKelaminEnum jenisKelamin;
}
