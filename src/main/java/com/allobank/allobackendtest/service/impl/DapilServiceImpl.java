package com.allobank.allobackendtest.service.impl;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.allobank.allobackendtest.entity.Dapil;
import com.allobank.allobackendtest.repository.DapilRepository;
import com.allobank.allobackendtest.service.DapilService;

@Service
public class DapilServiceImpl implements DapilService {

    @Autowired
    private DapilRepository dapilRepository;

    @Override
    public Dapil saveDapil(Dapil dapil) {
        return dapilRepository.save(dapil);
    }

    @Override
    public Dapil getDapilById(Long id) {
        return dapilRepository.findById(id).orElse(null);
    }

    @Override
    public List<Dapil> getAllDapils() {
        return dapilRepository.findAll();
    }

    @Override
    public void deleteDapil(Long id) {
        dapilRepository.deleteById(id);
    }

}
