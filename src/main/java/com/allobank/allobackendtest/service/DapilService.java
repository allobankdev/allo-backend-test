package com.allobank.allobackendtest.service;

import com.allobank.allobackendtest.model.Dapil;
import com.allobank.allobackendtest.util.dto.DapilRequestDTO;
import com.allobank.allobackendtest.util.dto.DapilResponseDTO;

import java.util.List;
import java.util.UUID;

public interface DapilService {
    List<Dapil> getAllDapil();
    DapilResponseDTO getDapilById(UUID id);
    DapilResponseDTO updateDapil(UUID id, DapilRequestDTO dapil);
    DapilResponseDTO createDapil(DapilRequestDTO dapil);
    void deleteDapil(UUID id);
}
