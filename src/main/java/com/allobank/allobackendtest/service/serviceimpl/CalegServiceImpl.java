package com.allobank.allobackendtest.service.serviceimpl;

import java.util.List;
import java.util.UUID;

import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.allobank.allobackendtest.model.Caleg;
import com.allobank.allobackendtest.model.Dapil;
import com.allobank.allobackendtest.model.Partai;
import com.allobank.allobackendtest.repository.CalegRepository;
import com.allobank.allobackendtest.repository.DapilRepository;
import com.allobank.allobackendtest.repository.PartaiRepository;
import com.allobank.allobackendtest.service.CalegService;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@Transactional
public class CalegServiceImpl implements CalegService {
    
   
    private final CalegRepository calegRepository;
    private final PartaiRepository partaiRepository;
    private final DapilRepository dapilRepository;

    @Autowired
    public CalegServiceImpl(CalegRepository calegRepository, PartaiRepository partaiRepository, DapilRepository dapilRepository) {
        this.calegRepository = calegRepository;
        this.partaiRepository = partaiRepository;
        this.dapilRepository = dapilRepository;
    }

    public List<Caleg> getCalegs(UUID dapilId, UUID partaiId, String sortBy) {
        List<Caleg> calegs;

        if (dapilId != null && partaiId != null) {
            calegs = calegRepository.findByDapilIdAndPartaiId(dapilId, partaiId);
        } else if (dapilId != null) {
            calegs = calegRepository.findByDapilId(dapilId);
        } else if (partaiId != null) {
            calegs = calegRepository.findByPartaiId(partaiId);
        } else {
            calegs = calegRepository.findAll();
        }

        if (sortBy != null && sortBy.equalsIgnoreCase("nomorUrut")) {
            calegs.sort((c1, c2) -> c1.getNomorUrut().compareTo(c2.getNomorUrut()));
        }

        return calegs;
    }

    public Caleg getCalegById(UUID id) {
        return calegRepository.findById(id).orElseThrow(() -> new RuntimeException("Caleg not found"));
    }
    
    public Caleg createCaleg(Caleg caleg) {

        try {
            Optional<Dapil> dapil = dapilRepository.findById(caleg.getDapilId());
            if (!dapil.isPresent()) {
                throw new RuntimeException("Dapil dengan ID " + caleg.getDapilId() + " tidak ditemukan.");
            }
    
           
            Optional<Partai> partai = partaiRepository.findById(caleg.getPartaiId());
            if (!partai.isPresent()) {
                throw new RuntimeException("Partai dengan ID " + caleg.getPartaiId() + " tidak ditemukan.");
            }
    
            if (caleg.getId() == null) {
                caleg.setId(UUID.randomUUID());
            }
            
            log.info("Caleg created successfully");
            return calegRepository.save(caleg);
        } catch (Exception e) {
            log.error("Error creating caleg: {}", e.getMessage());
            throw new RuntimeException(e.getMessage());
        }
    }
}