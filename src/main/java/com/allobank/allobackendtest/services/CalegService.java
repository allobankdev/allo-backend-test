package com.allobank.allobackendtest.services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.allobank.allobackendtest.model.Caleg;
import com.allobank.allobackendtest.model.Dapil;

import com.allobank.allobackendtest.model.Partai;

public interface CalegService {
    Page<Caleg> getCalegByDapilAndPartai(Dapil dapil, Partai partai, Pageable pageable);

    Page<Caleg> getAllCaleg(Pageable pageable);
}
