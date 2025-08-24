package com.allobank.allobackendtest.model;

import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class Partai {
    private UUID id;
    private String namaPartai;
    private Integer nomorUrut;
}
