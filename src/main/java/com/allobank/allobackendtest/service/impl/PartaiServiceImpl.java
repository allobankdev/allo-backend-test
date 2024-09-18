package com.allobank.allobackendtest.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.allobank.allobackendtest.entity.Partai;
import com.allobank.allobackendtest.repository.PartaiRepository;
import com.allobank.allobackendtest.service.PartaiService;

@Service
public class PartaiServiceImpl implements PartaiService {

    @Autowired
    private PartaiRepository partaiRepository;

    @Override
    public Partai savePartai(Partai partai) {
        return partaiRepository.save(partai);
    }

    @Override
    public Partai getPartaiById(Long id) {
        return partaiRepository.findById(id).orElse(null);
    }

    @Override
    public List<Partai> getAllPartais() {
        return partaiRepository.findAll();
    }

    @Override
    public void deletePartai(Long id) {
        partaiRepository.deleteById(id);
    }
}
