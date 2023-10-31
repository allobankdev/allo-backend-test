package com.allobank.allobackendtest.model;

import lombok.Data;

import java.util.UUID;

@Data
public class Partai {
    private UUID id;
    private String namaPartai;
    private Integer nomorUrut;
}
