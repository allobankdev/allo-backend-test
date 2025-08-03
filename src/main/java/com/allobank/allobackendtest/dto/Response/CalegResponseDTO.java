package com.allobank.allobackendtest.dto.Response;

import com.allobank.allobackendtest.entity.JenisKelaminEnum;
import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
public class CalegResponseDTO {
    private UUID id;
    private String nama;
    private Integer nomorUrut;
    private JenisKelaminEnum jenisKelamin;

    private PartaiDTO partai;
    private DapilDTO dapil;

    @Data
    public static class PartaiDTO {
        private UUID id;
        private String namaPartai;
        private Integer nomorUrut;
    }

    @Data
    public static class DapilDTO {
        private UUID id;
        private String namaDapil;
        private String provinsi;
        private List<String> wilayahDapilList;
        private int jumlahKursi;
    }
}