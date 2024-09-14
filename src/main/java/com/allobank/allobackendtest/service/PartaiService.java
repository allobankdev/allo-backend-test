package com.allobank.allobackendtest.service;

import java.util.List;
import java.util.UUID;

import com.allobank.allobackendtest.model.Partai;

public interface PartaiService {
    Partai createPartai(Partai partai);
    List<Partai> getAllPartai();
    Partai getPartaiById(UUID id);
}