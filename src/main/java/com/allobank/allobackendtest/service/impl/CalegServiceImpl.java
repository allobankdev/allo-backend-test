package com.allobank.allobackendtest.service.impl;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.allobank.allobackendtest.dto.CalegFilterDTO;
import com.allobank.allobackendtest.entity.Caleg;
import com.allobank.allobackendtest.entity.Dapil;
import com.allobank.allobackendtest.entity.Partai;
import com.allobank.allobackendtest.repository.CalegRepository;
import com.allobank.allobackendtest.repository.DapilRepository;
import com.allobank.allobackendtest.repository.PartaiRepository;
import com.allobank.allobackendtest.service.CalegService;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;

@Service
public class CalegServiceImpl implements CalegService {

    @Autowired
    private CalegRepository calegRepository;

    @Autowired
    private DapilRepository dapilRepository;

    @Autowired
    private PartaiRepository partaiRepository;

    @Transactional
    @Override
    public List<Caleg> getAllCaleg(CalegFilterDTO filterDTO) {
        List<Caleg> listData = new ArrayList<>();

        try {
            // Ambil parameter dari DTO
            Long dapilId = filterDTO.getDapilId();
            Long partaiId = filterDTO.getPartaiId();
            
            // Cari Dapil berdasarkan ID
            Dapil dapil = dapilRepository.findById(dapilId).orElse(null);
            if (dapil == null) {
                throw new EntityNotFoundException("Dapil with ID " + dapilId + " not found.");
            }
    
            // Cari Partai berdasarkan ID
            Partai partai = partaiRepository.findById(partaiId).orElse(null);
            if (partai == null) {
                throw new EntityNotFoundException("Partai with ID " + partaiId + " not found.");
            }
    
            // Ambil data Caleg berdasarkan Dapil dan Partai
            listData = calegRepository.findByDapilAndPartaiOrdered(dapil.getDapilId(), partai.getPartaiId());
    
        } catch (Exception e) {
            // Log the exception and handle it as needed
            e.printStackTrace(); // You might want to use a logger here
            throw new RuntimeException("Error occurred while fetching Caleg data: " + e.getMessage());
        }
        
        return listData;
    }

}
