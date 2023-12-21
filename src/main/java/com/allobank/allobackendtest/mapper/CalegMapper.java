package com.allobank.allobackendtest.mapper;
import com.allobank.allobackendtest.entity.CalegEntity;
import com.allobank.allobackendtest.model.Caleg;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.stream.Collectors;

public class CalegMapper {


    public static Caleg toCaleg(CalegEntity entity) {
        Caleg dto = new Caleg();
        dto.setId(entity.getId());
        dto.setNama(entity.getNama());
        dto.setNomorUrut(entity.getNomorUrut());
        dto.setDapil(DapilMapper.toDapil(entity.getDapil()));
        dto.setPartai(PartaiMapper.toPartai(entity.getPartai()));
        dto.setJenisKelamin(entity.getJenisKelamin());
        return dto;
    }

    public static List<Caleg> toCalegList(List<CalegEntity> entities) {
        return entities.stream()
                .map(CalegMapper::toCaleg)
                .collect(Collectors.toList());
    }

}
