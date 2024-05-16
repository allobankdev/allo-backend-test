package com.allobank.allobackendtest.service;

import com.allobank.allobackendtest.model.Partai;
import com.allobank.allobackendtest.repository.PartaiRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class PartaiService {

    @Autowired
    private PartaiRepository partaiRepository;

    public List<Partai> getAllPartai() {
        return partaiRepository.findAll();
    }

    public Partai createPartai(Partai partai) {
        return partaiRepository.save(partai);
    }

    public Partai updatePartai(UUID id, Partai partai) {
        partai.setId(id);
        return partaiRepository.save(partai);
    }

}

