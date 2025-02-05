package com.allobank.allobackendtest.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Partai {
    private UUID id;
    private String namaPartai;
    private Integer nomorUrut;
}
