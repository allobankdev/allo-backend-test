package com.allobank.allobackendtest.service.serviceimpl;

import java.util.UUID;

import org.springframework.stereotype.Service;

import com.allobank.allobackendtest.model.Dapil;
import com.allobank.allobackendtest.repository.DapilRepository;
import com.allobank.allobackendtest.service.DapilService;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@Transactional
public class DapilServiceImpl implements DapilService {
    
    private final DapilRepository dapilRepository;

    public DapilServiceImpl(DapilRepository dapilRepository) {
        this.dapilRepository = dapilRepository;
    }

    public Dapil createDapil(Dapil dapil) {
        try {
            if (dapil.getId() == null) {
                dapil.setId(UUID.randomUUID());
            }
            log.info("Dapil created");
            return dapilRepository.save(dapil);
        } catch (Exception e) {
            log.error("Failed to create dapil", e);
            throw new RuntimeException("Failed to create dapil");
        }
    }
}