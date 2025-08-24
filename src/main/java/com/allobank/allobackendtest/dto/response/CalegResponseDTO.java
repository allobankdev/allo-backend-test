package com.allobank.allobackendtest.dto.response;

import com.allobank.allobackendtest.model.Dapil;
import com.allobank.allobackendtest.model.JenisKelamin;
import com.allobank.allobackendtest.model.Partai;
import lombok.Data;

@Data
public class CalegResponseDTO {
    private String nama;
    private Integer nomorUrut;
    private JenisKelamin jenisKelamin;
    private PartaiResponseDTO partai;
    private DapilResponseDTO dapil;
}
