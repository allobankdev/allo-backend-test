package com.allobank.allobackendtest.service;

import com.allobank.allobackendtest.model.Caleg;
import com.allobank.allobackendtest.repository.CalegRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.UUID;

@Service
public class CalegService {

    @Autowired
    private CalegRepository calegRepository;

    public List<Caleg> getAllCaleg() {
        return calegRepository.findAll();
    }

    public List<Caleg> getCalegByDapil(UUID dapilId) {
        return calegRepository.findByDapil_Id(dapilId);
    }

    public List<Caleg> getCalegByPartai(UUID partaiId) {
        return calegRepository.findByPartai_Id(partaiId);
    }

    public List<Caleg> getCalegByDapilAndPartai(UUID dapilId, UUID partaiId) {
        return calegRepository.findByDapil_IdAndPartai_Id(dapilId, partaiId);
    }

    public List<Caleg> getCalegSortedByNomorUrut() {
        return calegRepository.findByNomorUrutOrderByNomorUrutAsc(1);
    }
}
