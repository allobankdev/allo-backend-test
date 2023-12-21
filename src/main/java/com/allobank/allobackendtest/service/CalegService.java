package com.allobank.allobackendtest.service;

import com.allobank.allobackendtest.model.Caleg;
import com.allobank.allobackendtest.model.Dapil;
import com.allobank.allobackendtest.model.Partai;
import com.allobank.allobackendtest.repository.CalegRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class CalegService {
    @Autowired
    private CalegRepository calegRepository;

    // Metode untuk mendapatkan semua Caleg
    public List<Caleg> getAllCaleg() {
        return calegRepository.findAll();
    }

    public List<Caleg> getCalegList(String dapilId, String partaiId, String sortBy, String sortOrder) {
        Sort sort = Sort.by(sortOrder.equalsIgnoreCase("DESC") ? Sort.Direction.DESC : Sort.Direction.ASC, sortBy);

        if (dapilId != null && partaiId != null) {
            return calegRepository.findByDapilIdAndPartaiId(dapilId, partaiId, sort);
        } else if (dapilId != null) {
            return calegRepository.findByDapilId(dapilId, sort);
        } else if (partaiId != null) {
            return calegRepository.findByPartaiId(partaiId, sort);
        } else {
            return calegRepository.findAllByOrderByNomorUrut(sort);
        }
    }
    public Caleg getCalegById(String id) {
        return calegRepository.findById(id).orElse(null);
    }

    public Caleg createCaleg(Caleg caleg) {
        return calegRepository.save(caleg);
    }

    public Caleg updateCaleg(String id, Caleg caleg) {
        if (calegRepository.existsById(id)) {
            caleg.setId(id);
            return calegRepository.save(caleg);
        } else {
            return null;
        }
    }

    public void deleteCaleg(String id) {
        calegRepository.deleteById(id);
    }
}
