package com.allobank.allobackendtest.mapper;

import com.allobank.allobackendtest.dto.Response.CalegResponseDTO;
import com.allobank.allobackendtest.entity.CalegEntity;

public class CalegMapper {

    public static CalegResponseDTO toDTO(CalegEntity entity) {
        if (entity == null) {
            return null;
        }

        CalegResponseDTO dto = new CalegResponseDTO();
        dto.setId(entity.getId());
        dto.setNama(entity.getNama());
        dto.setNomorUrut(entity.getNomorUrut());
        dto.setJenisKelamin(entity.getJenisKelamin());

        if (entity.getPartai() != null) {
            CalegResponseDTO.PartaiDTO partaiDTO = new CalegResponseDTO.PartaiDTO();
            partaiDTO.setId(entity.getPartai().getId());
            partaiDTO.setNamaPartai(entity.getPartai().getNamaPartai());
            partaiDTO.setNomorUrut(entity.getPartai().getNomorUrut());
            dto.setPartai(partaiDTO);
        }

        if (entity.getDapil() != null) {
            CalegResponseDTO.DapilDTO dapilDTO = new CalegResponseDTO.DapilDTO();
            dapilDTO.setId(entity.getDapil().getId());
            dapilDTO.setNamaDapil(entity.getDapil().getNamaDapil());
            dapilDTO.setProvinsi(entity.getDapil().getProvinsi());
            dapilDTO.setWilayahDapilList(entity.getDapil().getWilayahDapilList());
            dapilDTO.setJumlahKursi(entity.getDapil().getJumlahKursi());
            dto.setDapil(dapilDTO);
        }

        return dto;
    }
}
