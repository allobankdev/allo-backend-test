package com.allobank.allobackendtest.service;

import com.allobank.allobackendtest.model.Caleg;
import com.allobank.allobackendtest.model.Dapil;
import com.allobank.allobackendtest.model.Partai;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.UUID;


public interface CalegService {
    Caleg createCaleg(Caleg caleg);

    Caleg getCalegById(UUID calegId);

    List<Caleg> getAllCaleg();

    List<Caleg> getAllCalegSort(Sort sort);

    List<Caleg> findByPartai(Partai partai);

    List<Caleg> findByDapil(Dapil dapil);

    Caleg updateCaleg(Caleg caleg);

    void deleteCaleg(UUID calegId);
}
