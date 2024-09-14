package com.allobank.allobackendtest.service;

import java.util.List;
import java.util.UUID;

import com.allobank.allobackendtest.model.Dapil;

public interface DapilService {
    Dapil createDapil(Dapil dapil);
    List<Dapil> getAllDapil();
    Dapil getDapilById(UUID id);
}