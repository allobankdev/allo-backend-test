package com.allobank.allobackendtest.service.impl;

import com.allobank.allobackendtest.entity.CalegEntity;
import com.allobank.allobackendtest.entity.DapilEntity;
import com.allobank.allobackendtest.entity.PartaiEntity;
import com.allobank.allobackendtest.entity.WilayahDapilEntity;
import com.allobank.allobackendtest.model.Caleg;
import com.allobank.allobackendtest.model.Dapil;
import com.allobank.allobackendtest.model.Partai;
import com.allobank.allobackendtest.repository.CalegRepository;
import com.allobank.allobackendtest.service.CalegService;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CalegServiceImpl implements CalegService {

    private final CalegRepository calegRepository;

    public CalegServiceImpl(CalegRepository calegRepository) {
        this.calegRepository = calegRepository;
    }

    @Override
    public List<Caleg> listCalegs(String namaDapil, String namaPartai, String sortOrder) {
        List<CalegEntity> calegEntities;

        Sort sort = sortOrder.equalsIgnoreCase("desc") ? Sort.by("nomorUrut").descending() : Sort.by("nomorUrut").ascending();

        if (namaDapil != null && namaPartai != null) {
            calegEntities = calegRepository.findByDapilEntity_NamaDapilAndPartaiEntity_NamaPartai(namaDapil, namaPartai, sort);
        } else if (namaDapil != null) {
            calegEntities = calegRepository.findByDapilEntity_NamaDapil(namaDapil, sort);
        } else if (namaPartai != null) {
            calegEntities = calegRepository.findByPartaiEntity_NamaPartai(namaPartai, sort);
        } else {
            calegEntities = calegRepository.findAll(sort);
        }

        return calegEntities.stream()
                .map(this::responseCaleg)
                .collect(Collectors.toList());
    }

    private Caleg responseCaleg(CalegEntity calegEntity) {
        return Caleg.builder()
                .id(calegEntity.getId())
                .nama(calegEntity.getNama())
                .jenisKelamin(calegEntity.getJenisKelamin())
                .nomorUrut(calegEntity.getNomorUrut())
                .partai(mapPartai(calegEntity.getPartaiEntity()))
                .dapil(mapDapil(calegEntity.getDapilEntity()))
                .build();
    }

    private Dapil mapDapil(DapilEntity dapilEntity) {
        return Dapil.builder()
                .id(dapilEntity.getId())
                .namaDapil(dapilEntity.getNamaDapil())
                .provinsi(dapilEntity.getProvinsi())
                .jumlahKursi(dapilEntity.getJumlahKursi())
                .wilayahDapilList(dapilEntity.getWilayahDapilEntityList().stream()
                        .map(WilayahDapilEntity::getNamaWilayah)
                        .toList())
                .build();
    }

    private Partai mapPartai(PartaiEntity partaiEntity) {
        return Partai.builder()
                .id(partaiEntity.getId())
                .namaPartai(partaiEntity.getNamaPartai())
                .nomorUrut(partaiEntity.getNomorUrut())
                .build();
    }
}
