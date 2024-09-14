package com.allobank.allobackendtest.service;

import java.util.UUID;
import java.util.List;

import com.allobank.allobackendtest.model.Caleg;

public interface CalegService {
    List<Caleg> getCalegs(UUID dapilId, UUID partaiId, String sortBy);
    Caleg getCalegById(UUID id);
    Caleg createCaleg(Caleg caleg);
}