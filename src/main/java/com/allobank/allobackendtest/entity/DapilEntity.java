package com.allobank.allobackendtest.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;
import java.util.UUID;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="dapil")
public class DapilEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(name = "nama_dapil")
    private String namaDapil;

    private String provinsi;

    @Column(name = "jumlah_kursi")
    private int jumlahKursi;

    @Column(name = "wilayah_dapil")
    @ElementCollection
    private List<String> wilayahDapilList;

    @OneToMany(mappedBy = "dapil")
    private List<CalegEntity> calegDapilList;

}
