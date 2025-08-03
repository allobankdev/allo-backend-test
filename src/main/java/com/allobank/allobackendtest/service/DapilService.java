package com.allobank.allobackendtest.service;

import com.allobank.allobackendtest.dto.Request.DapilRequestDTO;
import com.allobank.allobackendtest.dto.Response.DapilResponseDTO;
import com.allobank.allobackendtest.entity.DapilEntity;
import com.allobank.allobackendtest.repository.DapilRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class DapilService {

    private final DapilRepository dapilRepository;

    public DapilResponseDTO create(DapilRequestDTO request) {
        DapilEntity entity = new DapilEntity();
        entity.setId(UUID.randomUUID());
        entity.setNamaDapil(request.getNamaDapil());
        entity.setProvinsi(request.getProvinsi());
        entity.setJumlahKursi(request.getJumlahKursi());
        entity.setWilayahDapilList(request.getWilayahDapilList());

        DapilEntity saved = dapilRepository.save(entity);

        DapilResponseDTO response = new DapilResponseDTO();
        response.setId(saved.getId());
        response.setNamaDapil(saved.getNamaDapil());
        response.setProvinsi(saved.getProvinsi());
        response.setJumlahKursi(saved.getJumlahKursi());
        response.setWilayahDapilList(saved.getWilayahDapilList());

        return response;
    }
}