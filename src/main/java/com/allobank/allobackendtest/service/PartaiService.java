package com.allobank.allobackendtest.service;

import java.util.List;
import org.springframework.stereotype.Service;

import com.allobank.allobackendtest.model.Partai;
import com.allobank.allobackendtest.repository.PartaiRepository;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import java.util.UUID;

@Service
@Transactional
@Slf4j
public class PartaiService {
    
    private final PartaiRepository partaiRepository;

    public PartaiService(PartaiRepository partaiRepository) {
        this.partaiRepository = partaiRepository;
    }

    public void createPartai(Partai partai) {
        try {
            var existedPartai = partaiRepository.findByNamaPartai(partai.getNamaPartai());
            var existedNomorUrut = partaiRepository.findByNomorUrut(partai.getNomorUrut());
            if (existedPartai != null || existedNomorUrut != null) {
                log.error("Partai already exist");
                throw new RuntimeException("Partai already exist");
            }
            
            if (partai.getId() == null) {
                partai.setId(UUID.randomUUID());
            }
            partaiRepository.save(partai);
            log.info("Partai created");
        } catch (Exception e) {
            log.error("Failed to create partai", e);
            throw new RuntimeException("Failed to create partai");
        }

    }

    public List<Partai> getAllPartai() {
        if (partaiRepository.findAll().isEmpty()) {
            log.error("Partai not found");
            throw new RuntimeException("Partai not found");
        }
        return partaiRepository.findAll();
    }
}