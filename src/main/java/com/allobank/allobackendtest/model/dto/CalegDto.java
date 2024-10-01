package com.allobank.allobackendtest.model.dto;

import com.allobank.allobackendtest.model.Caleg;
import com.allobank.allobackendtest.model.Dapil;
import com.allobank.allobackendtest.model.JenisKelamin;
import com.allobank.allobackendtest.model.Partai;

import java.util.UUID;

import lombok.Data;

@Data
public class CalegDto {
    private UUID id;
    private Dapil dapil;
    private Partai partai;
    private Integer nomorUrut;
    private String nama;
    private JenisKelamin jenisKelamin;

    public static CalegDto from(Caleg caleg) {
        CalegDto calegDto = new CalegDto();
        calegDto.setId(caleg.getId());
        calegDto.setDapil(caleg.getDapil());
        calegDto.setPartai(caleg.getPartai());
        calegDto.setNomorUrut(caleg.getNomorUrut());
        calegDto.setNama(caleg.getNama());
        calegDto.setJenisKelamin(caleg.getJenisKelamin());
        return calegDto;
    }
}
