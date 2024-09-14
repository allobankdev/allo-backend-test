package com.allobank.allobackendtest.service;

import java.util.List;

import com.allobank.allobackendtest.model.Partai;

public interface PartaiService {
    Partai createPartai(Partai partai);
    List<Partai> getAllPartai();
}