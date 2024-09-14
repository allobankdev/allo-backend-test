package com.allobank.allobackendtest.service.serviceimpl;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.allobank.allobackendtest.model.Partai;
import com.allobank.allobackendtest.repository.PartaiRepository;
import com.allobank.allobackendtest.service.PartaiService;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;

@Service
@Transactional
@Slf4j
public class PartaiServiceImpl implements PartaiService {
    
    private final PartaiRepository partaiRepository;

    public PartaiServiceImpl(PartaiRepository partaiRepository) {
        this.partaiRepository = partaiRepository;
    }

    public Partai createPartai(Partai partai) {
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
            log.info("Partai created");
            return partaiRepository.save(partai);
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

    @Override
    public Partai getPartaiById(UUID id) {
        return partaiRepository.findById(id).orElseThrow(() -> new RuntimeException("Partai not found"));
    }
}