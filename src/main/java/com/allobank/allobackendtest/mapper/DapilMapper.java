package com.allobank.allobackendtest.mapper;

import com.allobank.allobackendtest.entity.CalegEntity;
import com.allobank.allobackendtest.entity.DapilEntity;
import com.allobank.allobackendtest.model.Caleg;
import com.allobank.allobackendtest.model.Dapil;

import java.util.List;
import java.util.stream.Collectors;

public class DapilMapper {

    public static Dapil toDapil(DapilEntity entity){
        Dapil dto = new Dapil();
        dto.setId(entity.getId());
        dto.setNamaDapil(entity.getNamaDapil());
        dto.setProvinsi(entity.getProvinsi());
        dto.setWilayahDapilList(entity.getWilayahDapilList());
        dto.setJumlahKursi(entity.getJumlahKursi());
        return dto;
    }

    public static List<Dapil> toDapilList(List<DapilEntity> entities) {
        return entities.stream()
                .map(DapilMapper::toDapil)
                .collect(Collectors.toList());
    }
}
