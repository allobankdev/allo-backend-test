package com.allobank.allobackendtest.services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.allobank.allobackendtest.model.Caleg;
import com.allobank.allobackendtest.model.Dapil;
import com.allobank.allobackendtest.model.Partai;
import com.allobank.allobackendtest.repository.CalegRepository;
import com.allobank.allobackendtest.services.CalegService;

@Service
public class CalegServiceImpl implements CalegService {

    @Autowired
    private CalegRepository calegRepository;

    @Override
    public Page<Caleg> getCalegByDapilAndPartai(Dapil dapil, Partai partai, Pageable pageable) {
        return calegRepository.findByDapilAndPartai(dapil, partai, pageable);
    }

    @Override
    public Page<Caleg> getAllCaleg(Pageable pageable) {
        return calegRepository.findAll(pageable);
    }

}
