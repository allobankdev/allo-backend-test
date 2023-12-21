package com.allobank.allobackendtest.mapper;

import com.allobank.allobackendtest.entity.DapilEntity;
import com.allobank.allobackendtest.entity.PartaiEntity;
import com.allobank.allobackendtest.model.Dapil;
import com.allobank.allobackendtest.model.Partai;

import java.util.List;
import java.util.stream.Collectors;

public class PartaiMapper {

    public static Partai toPartai(PartaiEntity entity){
        Partai dto = new Partai();
        dto.setId(entity.getId());
        dto.setNomorUrut(entity.getNomorUrut());
        dto.setNamaPartai(entity.getNamaPartai());
        return dto;
    }

    public static List<Partai> toPartaiList(List<PartaiEntity> entities) {
        return entities.stream()
                .map(PartaiMapper::toPartai)
                .collect(Collectors.toList());
    }
}
