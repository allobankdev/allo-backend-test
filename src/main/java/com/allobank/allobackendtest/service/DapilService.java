package com.allobank.allobackendtest.service;

import com.allobank.allobackendtest.model.Dapil;
import com.allobank.allobackendtest.repository.DapilRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DapilService {

    @Autowired
    private DapilRepository dapilRepository;

    public List<Dapil> getAllDapils() {
        return dapilRepository.findAll();
    }

    public Optional<Dapil> getDapilById(String id) {
        return dapilRepository.findById(id);
    }

    public Dapil createDapil(Dapil dapil) {
        return dapilRepository.save(dapil);
    }

    public Dapil updateDapil(String id, Dapil dapil) {
        Optional<Dapil> existingDapil = dapilRepository.findById(id);
        if (existingDapil.isPresent()) {
            dapil.setId(id);
            return dapilRepository.save(dapil);
        } else {
            throw new IllegalArgumentException("Dapil with ID " + id + " not found.");
        }
    }

    public void deleteDapil(String id) {
        dapilRepository.deleteById(id);
    }
}

