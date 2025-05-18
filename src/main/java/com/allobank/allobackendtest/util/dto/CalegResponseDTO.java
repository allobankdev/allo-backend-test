package com.allobank.allobackendtest.util.dto;

import com.allobank.allobackendtest.model.Caleg;
import com.allobank.allobackendtest.model.JenisKelamin;
import lombok.Data;

@Data
public class CalegResponseDTO {
    public String nama;
    public int nomorUrut;
    public String namaDapil;
    public String namaPartai;
    public JenisKelamin jenisKelamin;

    public CalegResponseDTO(Caleg caleg) {
        this.nama = caleg.getNama();
        this.nomorUrut = caleg.getNomorUrut();
        this.namaDapil = caleg.getDapil().getNamaDapil();
        this.namaPartai = caleg.getPartai().getNamaPartai();
        this.jenisKelamin = caleg.getJenisKelamin();
    }

    public CalegResponseDTO() {

    }
}

