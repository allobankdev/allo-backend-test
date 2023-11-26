package com.allobank.allobackendtest.service;

import com.allobank.allobackendtest.model.Caleg;
import com.allobank.allobackendtest.model.Dapil;
import com.allobank.allobackendtest.model.Partai;
import com.allobank.allobackendtest.repository.CalegRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
@Service
@AllArgsConstructor
public class CalegServiceImpl implements CalegService{

    private CalegRepository calegRepository;

    @Override
    public Caleg createCaleg(Caleg caleg) {
        return calegRepository.save(caleg);
    }

    @Override
    public Caleg getCalegById(UUID calegId) {
        return calegRepository.findById(calegId)
                .orElseThrow(() -> new RuntimeException("Caleg not found with id " + calegId));
    }


    @Override
    public List<Caleg> getAllCaleg() {
        return calegRepository.findAll();
    }

    @Override
    public List<Caleg> getAllCalegSort(Sort sort) {
        return calegRepository.findAll(sort);
    }

    @Override
    public List<Caleg> findByPartai(Partai partai) {
        return calegRepository.findByPartai(partai);
    }

    @Override
    public List<Caleg> findByDapil(Dapil dapil) {
        return calegRepository.findByDapil(dapil);
    }

    @Override
    public Caleg updateCaleg(Caleg caleg) {
        return calegRepository.findById(caleg.getId())
                .map(dataCaleg -> {
                    dataCaleg.setNama(caleg.getNama());
                    dataCaleg.setNomorUrut(caleg.getNomorUrut());
                    dataCaleg.setJenisKelamin(caleg.getJenisKelamin());
                    return dataCaleg;  // Changes will be automatically saved at transaction commit
                })
                .orElseThrow(() -> new RuntimeException("Caleg not found with id " + caleg.getId()));
    }


    @Override
    public void deleteCaleg(UUID calegId) {
        calegRepository.deleteById(calegId);
    }
}
