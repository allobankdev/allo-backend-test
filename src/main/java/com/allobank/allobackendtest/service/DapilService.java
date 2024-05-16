package com.allobank.allobackendtest.service;

import com.allobank.allobackendtest.model.Dapil;
import com.allobank.allobackendtest.repository.DapilRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class DapilService {

    @Autowired
    private DapilRepository dapilRepository;

    public List<Dapil> getAllDapil() {
        return dapilRepository.findAll();
    }

    public Dapil createDapil(Dapil dapil) {
        return dapilRepository.save(dapil);
    }

    public Dapil updateDapil(UUID id, Dapil dapil) {
        dapil.setId(id);
        return dapilRepository.save(dapil);
    }
}

