package com.allobank.allobackendtest.service;

import org.springframework.stereotype.Service;

import com.allobank.allobackendtest.model.Dapil;
import com.allobank.allobackendtest.repository.DapilRepository;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import java.util.UUID;

@Slf4j
@Service
@Transactional
public class DapilService {
    
    private final DapilRepository dapilRepository;

    public DapilService(DapilRepository dapilRepository) {
        this.dapilRepository = dapilRepository;
    }

    public void createDapil(Dapil dapil) {
        try {
            if (dapil.getId() == null) {
                dapil.setId(UUID.randomUUID());
            }
            dapilRepository.save(dapil);
            log.info("Dapil created");
        } catch (Exception e) {
            log.error("Failed to create dapil", e);
            throw new RuntimeException("Failed to create dapil");
        }
    }
}