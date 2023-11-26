package com.allobank.allobackendtest.service;

import com.allobank.allobackendtest.model.Dapil;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.UUID;

public interface DapilService {
    Dapil createDapil(Dapil dapil);

    Dapil getDapilById(UUID dapilId);

    List<Dapil> getAllDapil();

    List<Dapil> getAllDapilSort(Sort sort);

    Dapil updateDapil(Dapil dapil);

    void deleteDapil(UUID dapilId);
}
