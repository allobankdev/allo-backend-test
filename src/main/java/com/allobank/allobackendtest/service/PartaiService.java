package com.allobank.allobackendtest.service;

import com.allobank.allobackendtest.entity.PartaiEntity;
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

    public List<PartaiEntity> getAllPartai() {
        return partaiRepository.findAll();
    }

}
