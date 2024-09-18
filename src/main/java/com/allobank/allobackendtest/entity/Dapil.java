package com.allobank.allobackendtest.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Entity
@Data
@Table(name = "dapil")
public class Dapil {
    
    @Id
    @SequenceGenerator(name="SEQ_DAPIL", sequenceName="dapil_id_seq", allocationSize=1)
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SEQ_DAPIL")
    @Column(name = "DAPIL_ID")
    private Long dapilId;

    @Column(name = "nama_dapil", nullable = false)
    private String namaDapil;

    @Column(name = "provinsi", nullable = false)
    private String provinsi;

    @ElementCollection
    @CollectionTable(name = "wilayah_dapil", joinColumns = @JoinColumn(name = "dapil_id"))
    @Column(name = "wilayah")
    private List<String> wilayahDapilList;

    @Column(nullable = false)
    private int jumlahKursi;
}
