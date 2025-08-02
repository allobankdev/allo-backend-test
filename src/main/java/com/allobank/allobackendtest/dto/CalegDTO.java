package com.allobank.allobackendtest.dto;

import com.allobank.allobackendtest.model.JenisKelamin;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CalegDTO {
    private UUID id;
    private DapilDTO dapil;
    private PartaiDTO partai;
    private Integer nomorUrut;
    private String nama;
    private JenisKelamin jenisKelamin;
}



