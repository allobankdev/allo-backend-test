package com.allobank.allobackendtest.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import java.util.List;
import java.util.UUID;

@Data
@Entity
@Table(name = "dapil")
public class DapilEntity {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "id", updatable = false, nullable = false, columnDefinition = "uuid")
    private UUID id;

    @Column(name = "nama_dapil")
    private String namaDapil;

    private String provinsi;

    @ElementCollection
    @CollectionTable(name = "dapil_wilayah", joinColumns = @JoinColumn(name = "dapil_id"))
    @Column(name = "wilayah")
    private List<String> wilayahDapilList;

    @Column(name = "jumlah_kursi")
    private int jumlahKursi;
}
