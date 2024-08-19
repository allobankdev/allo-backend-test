package com.allobank.allobackendtest.model;

import lombok.Data;

import java.util.List;

import com.allobank.allobackendtest.util.StringListConverter;

import jakarta.persistence.*;

@Data
@Entity
@Table(name = "dapil")
public class Dapil {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    private String namaDapil;
    private String provinsi;

    @Convert(converter = StringListConverter.class)
    private List<String> wilayahDapilList;

    private int jumlahKursi;
}
