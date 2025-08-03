package com.allobank.allobackendtest.model;


import com.allobank.allobackendtest.converter.JPAConverterJson;
import jakarta.persistence.*;
import lombok.Data;
import jakarta.persistence.Convert;

import java.util.List;




@Data
@Entity
@Table(name = "dapil")
public class Dapil {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private String id;
    @Column(name = "nama_dapil", unique = true)
    private String namaDapil;
    private String provinsi;
    @Convert(converter = JPAConverterJson.class)
    @Column(name = "wilayah_dapil_list", columnDefinition = "TEXT")
    private List<String> wilayahDapilList;
    private int jumlahKursi;


}
